<script setup lang="ts">
import type PostSummary from "~/types/PostSummary";

const { t, locale } = useI18n();

defineProps({
  post: {
    type: Object as () => PostSummary,
    required: true,
  },
});
</script>

<template>
  <article class="post-card">
    <div class="post-main-info">
      <NuxtLink :to="`/post/${post.id}`">
        <p>
          {{ post.content.substring(0, 50) }}
        </p>
      </NuxtLink>
    </div>
    <div class="post-other-info">
      <div class="author-info">
        <NuxtLink :to="`/user/${post.authorId}`">
          {{ post.authorName }}
        </NuxtLink>
      </div>
      <button class="like-button">
        <Icon name="mdi-light:heart" size="22" />
      </button>
      <div class="like-count">
        <span>{{ post.likeCount }}</span>
      </div>
      <button class="reply-button">
        <Icon name="mdi-light:comment" size="20" />
      </button>
      <div class="reply-count">
        <span>
          {{ " " + post.postCount }}
        </span>
      </div>
      <div class="created-at">
        <NuxtTime
          :datetime="post.createdAt"
          :relative="true"
          :locale="locale"
        />
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
.post-main-info {
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
.post-other-info {
  display: flex;
  justify-content: space-between;
}
.author-info {
  a {
    display: inline-block;
    padding-top: 0.1rem;
    text-decoration: none;
    color: #000;
  }
}
.like-button {
  border: 0;
  padding-right: 0;
  margin-left: auto;
  padding-top: 0.1rem;
  background-color: transparent;
}
.like-count {
  span {
    font-size: small;
    display: inline-block;
    padding-top: 0.5rem;
  }
  padding-right: 0.8rem;
}
.reply-button {
  border: 0;
  padding-top: 0.2rem;
  background-color: transparent;
}
.reply-count {
  span {
    font-size: small;
    display: inline-block;
    padding-top: 0.5rem;
  }
}
.created-at {
  width: 7rem;
  text-align-last: right;
  time {
    display: inline-block;
    padding-top: 0.5rem;
    font-size: smaller;
    color: gray;
    text-align: end;
  }
}
</style>
