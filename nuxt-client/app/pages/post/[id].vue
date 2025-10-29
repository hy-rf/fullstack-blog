<script setup lang="ts">
import type PostSummary from "~/types/PostSummary";

const { gtag } = useGtag();

const route = useRoute();
const postId = route.params.id as string;

onMounted(() => {
  if (posts.value) {
    gtag("event", "open_post", {
      post_id: postId,
    });
  }
});

const { data: posts } = await useFetch<PostSummary[]>(
  `/api/post/${route.params.id}`,
);

const userStore = useUserStore();
const postsToShow = ref<PostSummary[]>(posts.value || []);

// const isPostEditable = computed(() => {
//   const currentUser = userStore.user;
//   const currentPost = posts.value?.findLast(
//     (post) => String(post.id) === postId,
//   );
//   if (!currentUser || !currentPost) return false;
//   return Number(currentUser.id) === Number(currentPost.authorId);
// });
async function refresh() {
  const p = await fetch(`/api/post/${route.params.id}`).then((r) => r.json());
  postsToShow.value = p;
}
</script>

<template>
  <section class="post-list" aria-label="Posts list">
    <PostCard :key="postsToShow[0].id" :post="postsToShow[0]" />
    <div v-if="userStore.isUser" style="margin-top: 1rem">
      <PostEditor
        :post-to-edit="{ id: null, content: '' }"
        :root-post-id="posts![0].rootPostId || posts![0].id"
        :parent-post-id="parseInt(postId)"
        :refresh="refresh"
      />
    </div>
    <PostCard
      v-for="post in postsToShow.filter((_, i) => i > 0)"
      :id="'post-card-' + post.id"
      :key="post.id"
      :post="post"
    />
  </section>
</template>

<style scoped>
.post-list {
  padding-top: 0.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding-bottom: 3rem;
}
</style>
