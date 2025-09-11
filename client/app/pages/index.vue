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

    postStore.posts = data; // This line prevents hydration mismatch
    postsRef.value = data;
  } finally {
    pending.value = false;
  }
}

if (import.meta.client) {
  if (postStore.posts.length == 0) {
    // Its 1st time visit home page through csr in a page lifecycle, because no post in postStore.
    console.info(
      "1st time visit home page through csr in a page lifecycle, call Upstream API.",
    );
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

// Load more feature
const isFetchingMore = ref(false);

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
const onScroll = () => {
  const el = listRef.value;
  if (isFetchingMore.value || !el) return;
  if (navigator.userAgent.includes("chrome")) {
    if (window.scrollY > 250) {
      isFetchingMore.value = true;
      postStore.posts.shift();
      isFetchingMore.value = false;
      return;
    }
  }
  const THRESHOLD = window.innerHeight + 500 + 51;
  const { scrollHeight } = el;
  if (window.scrollY >= scrollHeight - THRESHOLD) {
    // console.log(`fetch, ${window.scrollY}, ${scrollHeight}, ${THRESHOLD}`);
    loadMorePosts();
  }
};

onMounted(async () => {
  document.addEventListener("scroll", onScroll);
  const options: ScrollToOptions = {
    top: postStore.scrollY,
  };
  // some times it simply not work on safari
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
      <PostCard
        v-for="post in postStore.posts || postsRef"
        :key="post.id"
        :post="post"
      />
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

@media screen and (min-width: 768px) {
  h1 {
    display: none;
  }
}
</style>
