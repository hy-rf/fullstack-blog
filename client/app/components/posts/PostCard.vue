<script setup lang="ts">
import type Post from "~/types/Post";

const { t } = useI18n();

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
        <h2>{{ post.title }}</h2>
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
      <div>
        <time :datetime="new Date(post.createdAt).toISOString()">
          {{
            new Date(post.createdAt)
              .toLocaleString("zh-TW", {
                year: "numeric",
                month: "2-digit",
                day: "2-digit",
                hour: "2-digit",
                minute: "2-digit",
                second: "2-digit",
                hour12: false,
                timeZone: "Asia/Taipei",
              })
              .replace(/\//g, "-")
              .replace(",", "")
              .replace(" ", " ")
          }}
        </time>
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
}
.post-other-info {
  display: flex;
  justify-content: space-between;

  time {
    font-size: smaller;
    color: gray;
  }
}
</style>
