<script setup lang="ts">
import type PostPage from "~/types/PostPage";

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
  $fetch(`/api/post/${route.params.id}`)
);

const userStore = useUserStore();

const isPostEditable = computed(() => {
  const currentUser = userStore.user;
  const currentPost = posts.value?.findLast(
    (post) => String(post.id) === postId
  );
  if (!currentUser || !currentPost) return false;
  return Number(currentUser.id) === Number(currentPost.authorId);
});

const showPostEditor = ref(false);
</script>

<template>
  <div class="container">
    <div v-for="post in posts" class="card">
      <div class="post-main">
        <div class="content" v-html="post.content"></div>
      </div>
      <div class="post-misc">
        <div class="post-created-at">
          <NuxtTime
            :datetime="post.createdAt"
            :relative="true"
            :locale="locale"
          />
        </div>
        <button v-if="isPostEditable" @click="showPostEditor = !showPostEditor">
          {{ t("post.edit_button") }}
        </button>
      </div>

      <div>
        <PostEditor
          v-if="showPostEditor"
          :post-to-edit="{
            content: post.content,
          }"
        />
      </div>

      <div class="author-section">
        <div class="author-info">
          <NuxtLink :to="`/user/${post.authorId}`">
            {{ post.authorName }}
          </NuxtLink>
        </div>
      </div>
    </div>
    <div v-if="userStore.isUser">
      You ar logged in user there will be a form to add a child a post
    </div>
  </div>
</template>

<style scoped>
.container {
  max-width: 800px;
  margin: 40px auto;
  padding: 0 20px;
  font-family: "Segoe UI", Tahoma, Geneva, Verdana, sans-serif;
  color: #333;
}

.card {
  background-color: #fff;
  padding: 30px;
  border-radius: 12px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.title {
  font-size: x-large;
  margin-bottom: 20px;
  color: #222;
}

.content {
  line-height: 1.7;
  margin-bottom: 20px;
}

.content h2,
.content h3,
.content p {
  margin-bottom: 16px;
}

.created-at {
  font-size: smaller;
  color: #777;
  margin-bottom: 20px;
  text-align: end;
}

hr {
  border: none;
  border-top: 1px solid #e0e0e0;
  margin: 24px 0;
}

.author-section {
  margin-top: 20px;
}

.subtitle {
  font-size: 1.25rem;
  margin-bottom: 10px;
  color: #444;
}

.author-info {
  list-style: none;
  padding: 0;
  margin: 0;
}

.error {
  background-color: #ffe6e6;
  color: #b00020;
  padding: 20px;
  border-radius: 8px;
  border: 1px solid #f5c2c2;
  text-align: center;
}

.loading {
  text-align: center;
  font-size: 1.1rem;
  color: #888;
  padding: 40px 0;
}

.reply-form {
  margin-top: 32px;
  background: #f7f9fa;
  padding: 20px;
  border-radius: 10px;
  box-shadow: 0 2px 8px rgba(99, 102, 241, 0.05);
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.reply-label {
  font-weight: 500;
  margin-bottom: 6px;
  color: #444;
}

.reply-textarea {
  width: 50%;
  padding: 10px;
  border-radius: 8px;
  border: 1px solid #d0d5dd;
  font-size: 1rem;
  background: #fff;
  resize: none;
}

.reply-btn {
  align-self: flex-end;
  border: none;
  border-radius: 8px;
  padding: 8px 24px;
  font-weight: 600;
  cursor: pointer;
}
.post-created-at {
  margin-left: auto;
  text-align: start;
  line-height: 34px;
  padding-right: 1rem;
}
.post-misc {
  display: flex;
}
</style>
