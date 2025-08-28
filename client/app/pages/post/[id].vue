<script setup lang="ts">
import type Post from "~/types/Post";

definePageMeta({
  validate: async (route) => {
    // Check if the id is made up of digits
    return typeof route.params.id === "string" && /^\d+$/.test(route.params.id);
  },
});
const { locale } = useI18n();
const { gtag } = useGtag();

onMounted(() => {
  if (post.value) {
    gtag("event", "open_post", {
      post_id: post.value.id,
      post_title: post.value.title,
      author_id: post.value.author.id,
    });
  }
});

const route = useRoute();
const postId = route.params.id as string;

// fetch not working when ssr
const { data: post, error } = await useAsyncData<Post>("post", () =>
  $fetch(`/api/post/${route.params.id}`)
);

const userStore = useUserStore();

const { t } = useI18n();

const replyContent = ref("");
const replyMessage = ref("");

const isPostEditable = computed(() => {
  const currentUser = userStore.user;
  const currentPost = post.value;
  if (!currentUser || !currentPost) return false;
  return Number(currentUser.id) === Number(currentPost.author?.id);
});

useSeoMeta({
  title: () => (post.value ? post.value.title : "Post"),
  description: () =>
    post.value ? post.value.content?.slice(0, 160) : "View post details",
  ogTitle: () => (post.value ? post.value.title : "Post"),
  ogDescription: () =>
    post.value ? post.value.content?.slice(0, 160) : "View post details",
  ogType: "article",
  ogUrl: `https://udevkit.lol/post/${postId}`,
  twitterCard: "summary",
  twitterTitle: () => (post.value ? post.value.title : "Post"),
  twitterDescription: () =>
    post.value ? post.value.content?.slice(0, 160) : "View post details",
});

const showEditPostForm = async () => {
  console.log(userStore.isUser, userStore.user.id, post.value?.author.id);
  console.log(userStore.isUser && userStore.user.id == post.value?.author.id);
  showPostEditor.value = !showPostEditor.value;
};

const showPostEditor = ref(false);

const submitReply = async () => {
  replyMessage.value = "";
  try {
    const res = await fetch("/api/reply", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        postId: postId,
        content: replyContent.value,
      }),
    });
    if (res.ok) {
      replyMessage.value = "Reply submitted!";
      replyContent.value = "";
      refreshReplies();
    } else {
      replyMessage.value = await res.text();
    }
  } catch (e) {
    replyMessage.value = "Network error";
  }
};

const refreshKey = ref(0);
function refreshReplies() {
  refreshKey.value++;
}
</script>

<template>
  <div class="container">
    <div v-if="post" class="card">
      <div class="post-main">
        <strong class="title">{{ post.title }}</strong>
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
        <button v-if="isPostEditable" @click="showEditPostForm">
          {{ t("post.edit_button") }}
        </button>
      </div>

      <div>
        <PostEditor
          v-if="showPostEditor"
          :post-to-edit="{
            title: post.title,
            content: post.content,
          }"
        />
      </div>

      <div class="author-section">
        <div class="author-info">
          <NuxtLink :to="`/user/${post.author.id}`">
            {{ post.author.username }}
          </NuxtLink>
        </div>
      </div>
      <div class="reply-section">
        <ReplyList :key="refreshKey" :replies="post.replies" />
      </div>
    </div>

    <div v-else-if="error" class="error">
      <p>Error loading post: {{ error.message }}</p>
    </div>

    <div v-else class="loading">
      <p>Loading...</p>
    </div>

    <div v-if="userStore.isUser">
      <form class="reply-form" @submit.prevent="submitReply">
        <label for="reply-content" class="reply-label">Add a reply:</label>
        <textarea
          id="reply-content"
          v-model="replyContent"
          class="reply-textarea"
          rows="3"
          required
        ></textarea>
        <button type="submit" class="reply-btn">Reply</button>
        <div v-if="replyMessage" class="reply-message">{{ replyMessage }}</div>
      </form>
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
