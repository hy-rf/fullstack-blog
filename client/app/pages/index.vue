<script setup lang="ts">
import PostCard from "~/components/post/PostCard.vue";
import type PostSummary from "~/types/PostSummary";

const { t, locale } = useI18n();

const { data: posts, pending } = useAsyncData<Array<PostSummary>>("post", () =>
  $fetch(`/api/post?page_token=1`)
);
</script>

<template>
  <h1>{{ t("home.feed") }}</h1>
  <section class="post-list" aria-label="Posts list">
    <PostCard
      v-for="post in posts"
      :key="post.id"
      :post="post"
      :id="'post-card-' + post.id"
    ></PostCard>
  </section>
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
