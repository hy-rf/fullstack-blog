<script setup lang="ts">
import { useHomePostStore } from "~/stores/home_post_sort";
import { fetchPosts } from "~/services/posts_service";
import PostCard from "~/components/post/PostCard.vue";
import type PostList from "~/types/PostList";

const { t, locale } = useI18n();
const route = useRoute();
const router = useRouter();
const homePostStore = useHomePostStore();

homePostStore.setFromRoute(route.query);

function toggleSortOrder() {
  if (homePostStore.order == "desc") {
    homePostStore.order = "asc";
  } else {
    homePostStore.order = "desc";
  }
  router.push({
    query: homePostStore.queryParams,
  });
}

const {
  data: posts,
  pending,
  refresh,
  error,
} = await useAsyncData<PostList>(
  () => `postsSearch-${JSON.stringify(route.query)}`,
  () => fetchPosts(route.query)
);

watch(
  () => route.query,
  (q) => {
    homePostStore.setFromRoute(q);
  }
);
</script>

<template>
  <h1>{{ t("home.feed") }}</h1>
  <div v-if="posts">
    <section class="toolbox">
      <div class="sort-options">
        <select
          v-model="homePostStore.sortBy"
          @change="
            router.push({
              query: homePostStore.queryParams,
            })
          "
        >
          <option value="createdAt">Created At</option>
          <option value="updatedAt">Updated At</option>
        </select>
        <button
          @click="toggleSortOrder"
          :style="{
            lineHeight: locale.startsWith('zh') ? '16px' : '',
            fontSize: locale.startsWith('zh') ? '14px' : '',
          }"
        >
          {{ homePostStore.order == "desc" ? t("home.desc") : t("home.asc") }}
        </button>
      </div>
    </section>
    <section class="post-list" aria-label="Posts list">
      <PostCard
        v-for="post in posts?.content"
        :key="post.id"
        :post="post"
      ></PostCard>
    </section>
  </div>
  <div v-if="pending">
    <p>Loading...</p>
  </div>
</template>

<style scoped>
.post-list {
  padding-top: 0.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
.toolbox {
  display: flex;
  flex-direction: row;
}
.sort-options {
  margin-left: auto;
  gap: 1rem;
  display: flex;
}
select {
  outline: none;
}
button {
  padding-block: 0.1rem;
  width: 6rem;
}
</style>
