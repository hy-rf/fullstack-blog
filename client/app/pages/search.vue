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
  <div>
    <h1>{{ t("posts.search.title") }}</h1>
    <div class="top-toolbar">
      <p>
        <ClientOnly>
          <span>{{ t("posts.search.time") }}</span>
          <time datetime="">{{ new Date().toLocaleString() }}</time>
        </ClientOnly>
      </p>
      <p>
        {{ posts!.totalElements }}
        <!-- {{
          t("posts.search.total_posts_1") + (posts == null)
            ? ""
            : posts!.totalElements + t("posts.search.total_posts_2")
        }} -->
      </p>
      <label for="pagesize">{{ t("posts.page_size") }}</label>
      <select id="pagesize" v-model="searchStore.size" @change="changeSize">
        <option :value="1">1</option>
        <option :value="5">5</option>
        <option :value="10">10</option>
        <option :value="20">20</option>
        <option :value="50">50</option>
      </select>
    </div>
    <div class="filters-container">
      <form
        class="filters"
        aria-label="Post search filters"
        @submit.prevent="performSearch"
      >
        <div class="search-fields">
          <input
            v-model="form.keyword"
            :placeholder="t('posts.search.keyword_content')"
          />
          <input
            v-model="form.authorName"
            :placeholder="t('posts.search.author')"
          />
        </div>
        <br />
        <!-- <div class="date-filters">
          <label
            >{{ t("posts.search.created_at_start") }}
            <input v-model="form.createdAfter" type="datetime-local" />
          </label>
          <label
            >{{ t("posts.search.created_at_end") }}
            <input v-model="form.createdBefore" type="datetime-local" />
          </label>
        </div> -->
        <div class="sort-options">
          <select v-model="form.sortBy">
            <option value="createdAt">
              {{ t("posts.search.created_at") }}
            </option>
            <option value="postCount">
              {{ t("posts.search.posts_count") }}
            </option>
            <option value="likeCount">
              {{ t("posts.search.likes_count") }}
            </option>
            <option value="saveCount">
              {{ t("posts.search.saves_count") }}
            </option>
          </select>
          <select v-model="form.order">
            <option value="desc">{{ t("posts.search.descending") }}</option>
            <option value="asc">{{ t("posts.search.ascending") }}</option>
          </select>
        </div>
        <button type="submit">{{ t("posts.search.button") }}</button>
      </form>
    </div>
    <div class="pagination" aria-label="Posts pagination">
      <button :disabled="searchStore.page <= 1" @click="prevPage">
        <Icon name="mdi-light:chevron-left" />
      </button>
      <span>{{ searchStore.page }}</span>
      <button
        :disabled="posts?.totalPages == searchStore.page"
        @click="nextPage"
      >
        <Icon name="mdi-light:chevron-right" />
      </button>
    </div>

    <div v-if="pending">Loading...</div>
    <div v-if="error">Error loading posts</div>
    <div v-if="posts?.numberOfElements === 0">No posts available</div>

    <div v-else class="search-result">
      <section aria-label="Posts list">
        <PostCard v-for="post in posts?.content" :key="post.id" :post="post" />
      </section>
    </div>
  </div>
</template>

<style scoped>
.top-toolbar {
  display: flex;
  padding-bottom: 0.5rem;
  & > p:nth-child(2) {
    margin-left: auto;
    color: #555555;
  }
}
.pagination {
  justify-content: center;
  display: flex;
  gap: 0.5rem;
  span {
    margin-block: auto;
  }
  button {
    width: 1.5rem;
    height: 1.5rem;
    border: 1px solid #333333;
    border-radius: 50%;
    display: flex;
    flex-direction: column;
    svg {
      margin: auto;
      position: relative;
      vertical-align: baseline;
    }
  }
}

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
.search-result {
  padding-bottom: 5rem;
}
.sort-options {
  display: flex;
  gap: 1rem;
}
</style>
