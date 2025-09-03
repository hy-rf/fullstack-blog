<script setup lang="ts">
import type PostSummary from "~/types/PostSummary";

const { t, locale } = useI18n();

const userStore = useUserStore();

const props = defineProps({
  post: {
    type: Object as () => PostSummary,
    required: true,
  },
});

const likeCount = ref(props.post.likeCount);
const saveCount = ref(props.post.saveCount);

const likePost = async (postId: number) => {
  const res = await fetch("/api/like", {
    method: "post",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ postId }),
  });
  if (res.status == 200) {
    likeCount.value++;
  }
};

const savePost = async (postId: number) => {
  const res = await fetch("/api/save-post", {
    method: "post",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ postId }),
  });
  if (res.status == 200) {
    saveCount.value++;
  }
};

const isSaved = userStore.savedPosts.includes(props.post.id)
</script>

<template>
  <article class="post-card">
    <div class="post-main">
      <NuxtLink :to="`/post/${post.id}`">
        <p>
          {{ post.content.substring(0, 50) }}
        </p>
      </NuxtLink>
    </div>
    <div class="post-other">
      <div class="author-info">
        <NuxtLink :to="`/user/${post.authorId}`">
          {{ post.authorName }}
        </NuxtLink>
      </div>
      <div class="post-other-right">
        <div>
          <button class="like-button" @click="likePost(post.id)">
            <Icon name="mdi-light:heart" size="20" />
          </button>
          <div class="like-count">
            <span>{{ likeCount }}</span>
          </div>
        </div>
        <div>
          <button class="save-button" @click="savePost(post.id)">
            <Icon name="mdi-light:bookmark" size="20" />
          </button>
          <div class="save-count">
            <span>{{ saveCount }}</span>
          </div>
        </div>
        <div>
          <button class="reply-button">
            <Icon name="mdi-light:comment" size="20" />
          </button>
          <div class="reply-count">
            <span>
              {{ " " + post.postCount }}
            </span>
          </div>
        </div>
        <div class="created-at">
          <NuxtTime
            :datetime="post.createdAt"
            :relative="true"
            :locale="locale"
          />
        </div>
      </div>
    </div>
    <slot></slot>
  </article>
</template>

<style lang="css" scoped>
.post-card {
  padding: 0.5rem 0 0.15rem 0;

  border-bottom: 1px solid #666666;
}
.post-main {
  gap: 0.5rem;
  padding-bottom: 0.5rem;
  a {
    color: black;
    text-decoration: none;
    re &:visited {
      color: #000;
    }
  }
}
.post-other {
  display: flex;
}
.author-info {
  a {
    display: inline-block;
    padding-top: 0.1rem;
    text-decoration: none;
    color: #000;
  }
}
.post-other-right {
  display: flex;
  margin-left: auto;
  gap: 0.8rem;
  div {
    display: flex;
    gap: 0.1rem;
    span {
      display: inline-block;
      vertical-align: bottom;
      padding-top: 0.5rem;
      font-size: 0.9rem;
    }
  }
}
button {
  border: 0;
  padding-top: 0.2rem;
  background-color: transparent;
}
.created-at {
  width: 5rem;
  text-align-last: right;
  time {
    display: inline-block;
    margin-left: auto;
    padding-top: 0.5rem;
    font-size: small;
    color: gray;
    text-align-last: end;
  }
}
</style>
