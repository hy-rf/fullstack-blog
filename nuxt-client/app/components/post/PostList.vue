<script setup lang="ts">
import PostCard from "./PostCard.vue";
import type PostPage from "~/types/PostPage";
const props = defineProps<{ posts: PostPage[] }>();
</script>
<template>
  <section class="post-list" aria-label="Posts list">
    <PostCard v-for="post in props.posts" :key="post.id" :post="post" />
  </section>
</template>

<style lang="css" scoped>
.post-list {
  position: relative;
  padding-top: 0.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
</style>
