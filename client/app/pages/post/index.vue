<script setup lang="ts">
import { usePostSearchStore } from "~/stores/post_search";
import { fetchPosts } from "~/services/posts_service";
import type Post from "~/types/Post";

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
  createdAfter: searchStore.createdAfter,
  createdBefore: searchStore.createdBefore,
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
} = await useAsyncData(
  () => `postsSearch-${JSON.stringify(route.query)}`,
  () => fetchPosts(route.query),
  { default: () => [] as Post[] }
);

// Watch route query → update store (and form)
watch(
  () => route.query,
  (q) => {
    searchStore.setFromRoute(q);
    Object.assign(form, {
      keyword: searchStore.keyword,
      authorName: searchStore.authorName,
      createdAfter: searchStore.createdAfter,
      createdBefore: searchStore.createdBefore,
      sortBy: searchStore.sortBy,
      order: searchStore.order,
      page: searchStore.page,
      size: searchStore.size,
    });
  }
);

// Change size → update store and query ( No search button click needed )
async function changeSize() {
  router.push({ query: searchStore.queryParams });
}

// Click search → copy form values into store → update query
async function performSearch() {
  searchStore.keyword = form.keyword;
  searchStore.authorName = form.authorName;
  searchStore.createdAfter = form.createdAfter;
  searchStore.createdBefore = form.createdBefore;
  searchStore.sortBy = form.sortBy;
  searchStore.order = form.order;
  searchStore.page = 1; // reset to first page
  await router.push({ query: searchStore.queryParams });
}

// Paging
function nextPage() {
  if (posts.value.length === searchStore.size) {
    searchStore.page++;
    router.push({ query: searchStore.queryParams });
  }
}
function prevPage() {
  if (searchStore.page > 1) {
    searchStore.page--;
    router.push({ query: searchStore.queryParams });
  }
}
</script>

<template>
  <div class="container">
    <h1>{{ t("posts.search") }}</h1>

    <!-- Filters bound to form state, not store, except  -->
    <div class="filters">
      <input v-model="form.keyword" placeholder="Search title/content..." />
      <input v-model="form.authorName" placeholder="Author name..." />
      <input type="datetime-local" v-model="form.createdAfter" />
      <input type="datetime-local" v-model="form.createdBefore" />
      <select v-model="form.sortBy">
        <option value="createdAt">Created At</option>
        <option value="updatedAt">Updated At</option>
      </select>
      <select v-model="form.order">
        <option value="desc">Descending</option>
        <option value="asc">Ascending</option>
      </select>
      <select v-model="searchStore.size" @change="changeSize">
        <option :value="1">1</option>
        <option :value="5">5</option>
        <option :value="10">10</option>
        <option :value="20">20</option>
        <option :value="50">50</option>
      </select>
      <button @click="performSearch">{{ t("posts.search") }}</button>
    </div>

    <!-- Results -->
    <div v-if="pending">Loading...</div>
    <div v-else-if="error">Error loading posts</div>
    <div v-else-if="posts.length === 0">No posts available</div>

    <div v-else>
      <div v-for="post in posts" :key="post.id" class="post-card">
        <NuxtLink :to="`/post/${post.id}`">
          <h2>{{ post.title }}</h2>
        </NuxtLink>
        <div>
          {{ t("posts.author") }}:
          <NuxtLink :to="`/user/${post.author.id}`">
            {{ post.author.username }}
          </NuxtLink>
          |
          <ClientOnly>
            {{ new Date(post.createdAt).toLocaleString() }}
          </ClientOnly>
        </div>
      </div>

      <!-- Pagination -->
      <div class="pagination">
        <button @click="prevPage" :disabled="searchStore.page <= 1">
          Prev
        </button>
        <span>Page {{ searchStore.page }}</span>
        <button @click="nextPage" :disabled="posts.length < searchStore.size">
          Next
        </button>
      </div>
    </div>
  </div>
</template>

<!-- <script setup lang="ts">
import type Post from "~/types/Post";

const { t } = useI18n();
const route = useRoute();
const router = useRouter();
const config = useRuntimeConfig();

useSeoMeta({
  title: t("posts.title"),
  // description: t("posts.searchDescription"),
  ogTitle: t("posts.title"),
  // ogDescription: t("posts.searchDescription"),
  ogType: "website",
  twitterCard: "summary",
});

const keyword = ref(route.query.keyword as string || "");
const authorName = ref(route.query.authorName as string || "");
const createdAfter = ref(route.query.createdAfter as string || "");
const createdBefore = ref(route.query.createdBefore as string || "");
const sortBy = ref(route.query.sortBy as string || "createdAt");
const order = ref(route.query.order as string || "desc");

const queryParams = computed(() => ({
  keyword: keyword.value || undefined,
  authorName: authorName.value || undefined,
  createdAfter: createdAfter.value || undefined,
  createdBefore: createdBefore.value || undefined,
  sortBy: sortBy.value,
  order: order.value,
}));

const { data: posts, pending: loading, refresh } = await useAsyncData(
  'postsSearch',
  // If use /api/posts/search it will call localhost:8080/posts/search (by nitro proxy)
  // So use the full URL to avoid that
  () => $fetch<Array<Post>>(`/api/posts/search`, {
    query: queryParams.value,
  }),
  {
    default: () => [],
  }
);

async function performSearch() {
  await router.push({ query: queryParams.value });
  await refresh();
  console.log("Search performed with params:", queryParams.value);
}

watch(
  () => route.query,
  (newQuery) => {
    // Update refs from route.query
    keyword.value = newQuery.keyword as string || "";
    authorName.value = newQuery.authorName as string || "";
    createdAfter.value = newQuery.createdAfter as string || "";
    createdBefore.value = newQuery.createdBefore as string || "";
    sortBy.value = newQuery.sortBy as string || "createdAt";
    order.value = newQuery.order as string || "desc";

    // Refresh data based on new query params
    refresh();
  },
  { immediate: true }
);
</script>

<template>
  <div class="container">
    <h1 class="page-title">{{ t("posts.search") }}</h1>

    <div class="filters">
      <input
        v-model="keyword"
        type="text"
        placeholder="Search title/content..."
      />
      <input v-model="authorName" type="text" placeholder="Author name..." />
      <input v-model="createdAfter" type="datetime-local" />
      <input v-model="createdBefore" type="datetime-local" />

      <select v-model="sortBy">
        <option value="createdAt">Created At</option>
        <option value="updatedAt">Updated At</option>
      </select>
      <select v-model="order">
        <option value="desc">Descending</option>
        <option value="asc">Ascending</option>
      </select>

      <button @click="performSearch" :disabled="false">
        {{ "Search" }}
      </button>
    </div>

    <div v-if="!loading && posts.length === 0" class="empty">
      No posts available.
    </div>

    <div v-else v-for="post in posts" :key="post.id" class="post-card">
      <NuxtLink :to="`/post/${post.id}`" class="post-title-link">
        <h2 class="post-title">{{ post.title }}</h2>
      </NuxtLink>

      <div class="meta">
        {{ t("posts.author") }}
        <NuxtLink :to="`/user/${post.author.id}`" class="author-link">
          {{ post.author.username }}
        </NuxtLink>
        |
        <ClientOnly>
          <span class="date">{{
            new Date(post.createdAt).toLocaleString()
          }}</span>
        </ClientOnly>
      </div>
      <hr />
    </div>
  </div>
</template> -->