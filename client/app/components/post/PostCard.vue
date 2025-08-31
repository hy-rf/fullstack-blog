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
        <h2>
          {{ post.content.substring(0, 50) }}
        </h2>
      </NuxtLink>
      <!-- <p v.html="post.content" class="post-content"></p> -->
    </div>
    <div class="post-other-info">
      <div class="author-info">
        <NuxtLink :to="`/user/${post.authorId}`">
          {{ post.authorName }}
        </NuxtLink>
      </div>
      <div class="reply-count">
        <span>{{ t("posts.reply_count") }}</span>
        {{ post.postCount }}
      </div>
      <div class="created-at">
        <NuxtTime
          :datetime="post.createdAt"
          :relative="true"
          :locale="locale"
        />
      </div>
    </div>
  </article>
</template>

<style lang="css" scoped>
.post-card {
  padding: 0.5rem;
  border: 1px solid #e0e0e0;
  border-radius: 0.5rem;
  transition: border 0.3s ease;
  &:hover {
    border: 1px solid #808080;
  }
}
.post-main-info {
  gap: 0.5rem;
  padding-bottom: 0.5rem;
}
.post-other-info {
  display: flex;
  justify-content: space-between;

  time {
    font-size: smaller;
    color: gray;
    text-align: end;
  }
}
.reply-count {
  margin-left: auto;
  margin-right: 1rem;
  font-size: small;
  padding-top: 4px;
}
.created-at {
  width: 7rem;
  text-align-last: right;
}
</style>
