<script setup lang="ts">
import { usePostSearchStore } from "~/stores/post_search";
import { fetchPosts } from "~/services/posts_service";
import PostCard from "~/components/post/PostCard.vue";
import type PostListViewModel from "~/types/PostList";

// These func will move to util
// Helper to convert local time (UTC+8) to UTC ISO string
function localToUTC(localValue: string): string {
  if (!localValue) return "";
  const localDate = new Date(localValue);
  return localDate.toISOString().replace(/\.000Z$/, "");
}
// When initializing form, convert UTC from backend to local for input
function utcToLocal(utcValue: string): string {
  if (!utcValue) return "";
  console.log(utcValue);

  const utcDate = new Date(utcValue + "Z");
  console.log(
    utcDate.toLocaleString(),
    utcDate.toLocaleString("zh-TW", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      hour12: false,
      timeZone: "Asia/Taipei",
    }),
  );

  return utcDate
    .toLocaleString("zh-TW", {
      year: "numeric",
      month: "2-digit",
      day: "2-digit",
      hour: "2-digit",
      minute: "2-digit",
      second: "2-digit",
      hour12: false,
      timeZone: "Asia/Taipei",
    })
    .replace(/\//g, "-")
    .replace(",", "")
    .replace(" ", "T");
}

const { t } = useI18n();
const route = useRoute();
const router = useRouter();
const searchStore = usePostSearchStore();

// Initialize store from URL on load
searchStore.setFromRoute(route.query);

// Form state (local only, not used by fetch until Search clicked)
const form = reactive({
  keyword: searchStore.keyword,
  authorName: searchStore.authorName,
  createdAfter: utcToLocal(searchStore.createdAfter),
  createdBefore: utcToLocal(searchStore.createdBefore),
  sortBy: searchStore.sortBy,
  order: searchStore.order,
  page: searchStore.page,
  size: searchStore.size,
});

// API fetching
const {
  data: posts,
  pending,
  refresh,
  error,
} = await useAsyncData<PostListViewModel>(
  () => `postsSearch-${JSON.stringify(route.query)}`,
  () => fetchPosts(route.query),
);

// Watch route query → update store (and form)
watch(
  () => route.query,
  (q) => {
    searchStore.setFromRoute(q);
    Object.assign(form, {
      keyword: searchStore.keyword,
      authorName: searchStore.authorName,
      createdAfter: utcToLocal(searchStore.createdAfter),
      createdBefore: utcToLocal(searchStore.createdBefore),
      sortBy: searchStore.sortBy,
      order: searchStore.order,
      page: searchStore.page,
      size: searchStore.size,
    });
  },
);

// Change size → update store and query ( No search button click needed )
async function changeSize() {
  router.push({
    query: searchStore.queryParams,
  });
}

// Click search → copy form values into store → update query
async function performSearch() {
  console.log(form);

  searchStore.keyword = form.keyword;
  searchStore.authorName = form.authorName;
  searchStore.createdAfter = localToUTC(form.createdAfter);
  searchStore.createdBefore = localToUTC(form.createdBefore);
  searchStore.sortBy = form.sortBy;
  searchStore.order = form.order;
  searchStore.page = 1; // reset to first page
  await router.push({
    query: searchStore.queryParams,
  });
}

// Paging
function nextPage() {
  searchStore.page++;
  router.push({
    query: searchStore.queryParams,
  });
}
function prevPage() {
  searchStore.page--;
  router.push({
    query: searchStore.queryParams,
  });
}
</script>

<template>
  <div class="container">
    <h1>{{ t("posts.search") }}</h1>
    <p>Total: {{ posts?.totalElements }} Posts</p>
    <!-- Filters bound to form state, not store, except  -->
    <div class="filters-container">
      <form
        class="filters"
        @submit.prevent="performSearch"
        aria-label="Post search filters"
      >
        <div class="search-fields">
          <input
            v-model="form.keyword"
            :placeholder="t('posts.filter.title_content')"
          />
          <input
            v-model="form.authorName"
            :placeholder="t('posts.filter.author')"
          />
        </div>
        <br />
        <div class="date-filters">
          <label
            >{{ t("posts.filter.created_at_start") }}
            <input type="datetime-local" v-model="form.createdAfter" />
          </label>
          <label
            >{{ t("posts.filter.created_at_end") }}
            <input type="datetime-local" v-model="form.createdBefore" />
          </label>
        </div>
        <div class="sort-options">
          <div>
            <select v-model="form.sortBy">
              <option value="createdAt">Created At</option>
              <option value="updatedAt">Updated At</option>
            </select>
            <select v-model="form.order">
              <option value="desc">Descending</option>
              <option value="asc">Ascending</option>
            </select>
          </div>
        </div>
        <button type="submit">{{ t("posts.search") }}</button>
      </form>
    </div>
    <label for="pagesize">{{ t("posts.page_size") }}</label>
    <select id="pagesize" v-model="searchStore.size" @change="changeSize">
      <option :value="1">1</option>
      <option :value="5">5</option>
      <option :value="10">10</option>
      <option :value="20">20</option>
      <option :value="50">50</option>
    </select>

    <!-- Results -->
    <div v-if="pending">Loading...</div>
    <div v-else-if="error">Error loading posts</div>
    <div v-else-if="posts?.numberOfElements === 0">No posts available</div>

    <div v-else>
      <section aria-label="Posts list">
        <PostCard
          v-for="post in posts?.content"
          :key="post.id"
          :post="post"
        ></PostCard>
      </section>

      <!-- Pagination -->
      <div class="pagination" aria-label="Posts pagination">
        <button @click="prevPage" :disabled="searchStore.page <= 1">
          Prev
        </button>
        <span>Page {{ searchStore.page }}</span>
        <button
          @click="nextPage"
          :disabled="posts?.totalPages == searchStore.page"
        >
          Next
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
.filters {
  display: flex;
  flex-wrap: wrap;
  gap: 10px;
  margin-bottom: 20px;
}
.filters input,
.filters select,
.filters button {
  padding: 8px;
  font-size: 0.9rem;
}
.filters button {
  background-color: #007bff;
  color: white;
  border: none;
  cursor: pointer;
}
.filters button:disabled {
  background-color: #ccc;
  cursor: not-allowed;
}
.filters-container {
  display: flex;
  flex-direction: row;
}
</style>
