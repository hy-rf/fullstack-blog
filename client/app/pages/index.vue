<script setup lang="ts">
import PostCard from "~/components/post/PostCard.vue";
import type PostSummary from "~/types/PostSummary";
const { t } = useI18n();
const config = useRuntimeConfig();

const postStore = useHomePostsStore();
const initialOffset = postStore.offset || 0;
const postsRef = ref<PostSummary[] | null>(null); // This is used in ssr for SEO
const pending = ref(false);

if (import.meta.server) {
  pending.value = true;
  try {
    const data = await $fetch<PostSummary[]>(
      `/api/post?offset=${initialOffset}`,
    );
    console.log(postStore.posts);

    // postStore.posts = data;
    postsRef.value = data;
  } finally {
    pending.value = false;
  }
} else {
  // This block only runs on client(browser)
  if (!postStore.posts || postStore.posts.length == 0) {
    pending.value = true;
    try {
      const data = await $fetch<PostSummary[]>(
        `${config.public.GATEWAY_URL}/post?offset=${initialOffset}`,
      );
      postStore.posts = data;
    } finally {
      pending.value = false;
    }
  } else {
    console.log("Revisit feed");
  }
}

// Related to Load more feature
const isFetchingMore = ref(false);
const threshold = 3000;

const loadMorePosts = async () => {
  if (isFetchingMore.value) return;
  isFetchingMore.value = true;
  postStore.offset += 50;
  try {
    const data = await fetch(
      `${config.public.GATEWAY_URL}/post?offset=${postStore.offset}`,
    ).then((r) => r.json());
    postStore.append(data);
  } finally {
    isFetchingMore.value = false;
  }
};
const listRef = ref<HTMLElement | null>(null);
const onScroll = async () => {
  const el = listRef.value;
  console.log(window.scrollY);
  if (window.scrollY - 80 > 100) {
    postStore.posts.shift();
  }
  // should not fetch while fetching
  if (isFetchingMore.value || !el) return;
  const { scrollHeight } = el;
  if (window.scrollY >= scrollHeight - threshold) {
    await loadMorePosts();
  }
};

onMounted(async () => {
  document.addEventListener("scroll", onScroll);
  const options: ScrollToOptions = {
    top: postStore.scrollY,
  };
  setTimeout(() => {
    window.scrollTo(options);
  }, 0);
});

onBeforeUnmount(() => {
  document.removeEventListener("scroll", onScroll);
  postStore.scrollY = window.scrollY;
});
</script>

<template>
  <div>
    <h1>{{ t("home.feed") }}</h1>
    <section ref="listRef" class="post-list" aria-label="Posts list">
      <PostCard v-for="post in postStore.posts" :key="post.id" :post="post" />
    </section>
  </div>
</template>

<style scoped>
.post-list {
  padding-top: 0.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding-bottom: 5rem;
}
.toolbox {
  display: flex;
  flex-direction: row;
}
.sort-options {
  margin-left: auto;
  gap: 1rem;
  display: flex;
}
select {
  outline: none;
}
button {
  padding-block: 0.1rem;
  width: 6rem;
}
</style>
