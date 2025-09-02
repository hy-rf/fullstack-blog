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
} = await useAsyncData<Array<PostPage>>(`post-${postId}`, () =>
  $fetch(`/api/post/${route.params.id}`),
);

const userStore = useUserStore();

const isPostEditable = computed(() => {
  const currentUser = userStore.user;
  const currentPost = posts.value?.findLast(
    (post) => String(post.id) === postId,
  );
  if (!currentUser || !currentPost) return false;
  return Number(currentUser.id) === Number(currentPost.authorId);
});

const showPostEditor = ref(false);
</script>

<template>
  <section class="post-list" aria-label="Posts list">
    <PostCard
      v-for="post in posts"
      :key="post.id"
      :post="post"
      :id="'post-card-' + post.id"
    >
    </PostCard>
    <div v-if="userStore.isUser">
      You ar logged in user there will be a form to add a child a post
    </div>
  </section>
</template>

<style scoped>
.post-list {
  padding-top: 0.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
</style>
