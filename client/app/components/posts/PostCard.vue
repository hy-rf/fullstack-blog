<script setup lang="ts">
import type Post from "~/types/Post";

const { t, locale } = useI18n();

defineProps({
  post: {
    type: Object as () => Post,
    required: true,
  },
});
</script>

<template>
  <article class="post-card">
    <div class="post-main-info">
      <NuxtLink :to="`/post/${post.id}`">
        <h2>
          {{ post.title }}
        </h2>
      </NuxtLink>
      <!-- <p v.html="post.content" class="post-content"></p> -->
    </div>
    <div class="post-other-info">
      <div>
        {{ t("posts.author") }}:
        <NuxtLink :to="`/user/${post.author.id}`">
          {{ post.author.username }}
        </NuxtLink>
      </div>
      <div class="reply-count">
        <span>{{ t("posts.reply_count") }}</span>
        {{ post.replyCount }}
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
</style>
