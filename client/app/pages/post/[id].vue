<script setup lang="ts">
import type PostPage from "~/types/PostPage";
// TODO: recursively render child posts
definePageMeta({
  validate: async (route) => {
    // Check if the id is made up of digits
    return typeof route.params.id === "string" && /^\d+$/.test(route.params.id);
  },
});
const { t, locale } = useI18n();
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

const {
  data: posts,
  status,
  error,
} = await useAsyncData<PostPage[]>(`post-${postId}`, () =>
  $fetch(`/api/post/${route.params.id}`),
);

const userStore = useUserStore();
const postsToShow = ref<PostPage[]>(posts.value || []);

const isPostEditable = computed(() => {
  const currentUser = userStore.user;
  const currentPost = posts.value?.findLast(
    (post) => String(post.id) === postId,
  );
  if (!currentUser || !currentPost) return false;
  return Number(currentUser.id) === Number(currentPost.authorId);
});
async function refresh() {
  const p = await fetch(`/api/post/${route.params.id}`).then((r) => r.json());
  postsToShow.value = p;
}
</script>

<template>
  <section class="post-list" aria-label="Posts list">
    <PostCard
      v-for="post in postsToShow"
      :key="post.id"
      :post="post"
      :id="'post-card-' + post.id"
    >
    </PostCard>
    <div v-if="userStore.isUser" style="margin-top: 1rem">
      <PostEditor
        :post-to-edit="{ id: null, content: '' }"
        :root-post-id="posts![0].rootPostId || posts![0].id"
        :post-id="parseInt(postId)"
        :refresh="refresh"
      />
    </div>
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
