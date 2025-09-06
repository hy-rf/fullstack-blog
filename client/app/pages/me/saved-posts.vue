<script setup lang="ts">
import type PostSummary from "~/types/PostSummary";
const { t } = useI18n();
const { data, pending } = useFetch<PostSummary[]>("/api/saved-posts");
const postsToShow = ref(data);
async function deleteSavedPost(postId: number) {
  const r = await fetch("/api/saved-post" + "?postId=" + postId, {
    method: "delete",
  });
  if (r.ok) {
    postsToShow.value = postsToShow.value?.filter((e) => e.id != postId)!;
  }
}
</script>

<template>
  <div>
    <div v-for="post in postsToShow" :key="post.id">
      <PostCard :post="post" />
      <button @click="deleteSavedPost(post.id)">
        {{ t("me.saved.delete_button") }}
      </button>
    </div>
    <div v-if="pending">Loading</div>
  </div>
</template>

<style lang="css" scoped></style>
