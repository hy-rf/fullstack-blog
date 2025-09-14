<script setup lang="ts">
import type PostSummary from "~/types/PostSummary";

const { locale } = useI18n();
const route = useRoute();
const postId = route.params.id as string;

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
  if (userStore.likedPosts.includes(postId)) {
    alert("Already liked this post");
    return;
  }
  const res = await fetch("/api/like", {
    method: "post",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ postId }),
  });
  if (res.status == 200) {
    likeCount.value++;
  }
  userStore.loadLikedPosts();
};

const savePost = async (postId: number) => {
  if (userStore.savedPosts.includes(postId)) {
    alert("Already saved this post");
    return;
  }
  const res = await fetch(`/api/save-post?post-id=${postId}`, {
    method: "post",
    headers: { "Content-Type": "application/json" },
  });
  if (res.status == 200) {
    saveCount.value++;
  }
  userStore.loadSavedPosts();
};

const replyToPost = async (postId: number) => {
  alert("Reply popup" + postId);
};

const isLiked = computed(() => userStore.likedPosts.includes(props.post.id));
const isSaved = computed(() => userStore.savedPosts.includes(props.post.id));
</script>

<template>
  <article class="post-card">
    <div class="post-main">
      <p style="font-size: smaller; padding-bottom: 0.3rem">#{{ post.id }}</p>

      <NuxtLink v-if="post.id.toString() !== postId" :to="`/post/${post.id}`">
        <p>
          {{ post.content }}
        </p>
      </NuxtLink>
      <p v-else>
        {{ post.content }}
      </p>
    </div>

    <div
      v-if="post.imageUrls && post.imageUrls.length"
      class="image-preview-container"
    >
      <div
        v-for="(url, index) in post.imageUrls"
        :key="index"
        class="image-wrapper"
      >
        <img :src="url" alt="Post Image" />
      </div>
    </div>

    <div class="post-other">
      <div class="author-info">
        <NuxtLink :to="`/user/${post.authorId}`">
          <span>
            {{ post.authorName }}
          </span>
        </NuxtLink>
      </div>
      <div class="post-other-right">
        <div class="post-other-right-item">
          <button class="like-button" @click="likePost(post.id)">
            <ClientOnly>
              <Icon v-if="!isLiked" name="mdi-light:heart" size="18" />
              <Icon
                v-if="isLiked"
                name="mdi-light:heart"
                size="18"
                style="color: red"
              />
            </ClientOnly>
          </button>
          <div class="count-numbers like-count">
            <span>{{ likeCount }}</span>
          </div>
        </div>
        <div class="post-other-right-item">
          <button class="save-button" @click="savePost(post.id)">
            <ClientOnly>
              <Icon
                v-if="isSaved == false"
                name="mdi-light:bookmark"
                size="18"
              />
              <Icon
                v-if="isSaved == true"
                name="mdi-light:bookmark"
                size="18"
                style="color: green"
              />
            </ClientOnly>
          </button>
          <div class="count-numbers save-count">
            <span>{{ saveCount }}</span>
          </div>
        </div>
        <div class="post-other-right-item">
          <button class="reply-button" @click="replyToPost(post.id)">
            <Icon name="mdi-light:comment" size="18" />
          </button>
          <div class="count-numbers reply-count">
            <span>
              {{ post.postCount }}
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
    <slot />
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
    span {
      font-size: smaller;
    }
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
.post-other-right-item {
  .count-numbers {
    padding-bottom: 0.2rem;
  }
}
button {
  border: 0;
  padding-top: 0.4rem;
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

/* Images style */
.image-preview-container {
  display: flex;
  flex-wrap: wrap;
  gap: 1rem;
  justify-content: center;
  margin-top: 1rem;
}

.image-wrapper {
  width: 200px;
  height: 200px;
  background-color: black;
  display: flex;
  align-items: center;
  justify-content: center;
  overflow: hidden;
  border-radius: 0.5rem;
}

.image-wrapper img {
  width: 100%;
  height: 100%;
  object-fit: contain;
}
</style>
