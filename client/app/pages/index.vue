<script setup lang="ts">
// TODO: save scroll info so that can keep after browsing other routes then back
import PostCard from "~/components/post/PostCard.vue";
import type PostSummary from "~/types/PostSummary";

const postStore = useHomePostsStore();
const { t, locale } = useI18n();

const { data: posts, pending } = useFetch<PostSummary[]>(`/api/post?offset=0`, {
  server: true,
});

if (import.meta.server && posts.value && postStore.posts.length === 0) {
  postStore.init(posts.value);
}

const postsToShow = computed(() => {
  if (postStore.posts && postStore.posts.length > 0) return postStore.posts;
  return posts.value ?? [];
});


const isFetchingMore = ref(false);
const threshold = 10;

const loadPosts = async () => {
  if (isFetchingMore.value) return;
  isFetchingMore.value = true;
  try {
    const data = await fetch(`/api/post?offset=${postStore.offset}`).then((r) =>
      r.json(),
    );
    postStore.append(data);
    postStore.offset += 50;
  } finally {
    isFetchingMore.value = false;
  }
};
const listRef = ref<HTMLElement | null>(null);
const onScroll = () => {
  const el = listRef.value;
  if (!el || isFetchingMore.value) return;
  const { scrollTop, clientHeight, scrollHeight } = el;

  if (scrollTop + clientHeight >= scrollHeight - threshold) {
    loadPosts();
  }
};

onMounted(() => {
  document.addEventListener("scroll", onScroll);
});

onBeforeRouteLeave(() => {
  document.removeEventListener("scroll", onScroll);
  console.log(listRef.value?.scrollTop);
  
});
</script>

<template>
  <h1>{{ t("home.feed") }}</h1>
  <section ref="listRef" class="post-list" aria-label="Posts list">
    <PostCard
      v-for="post in postsToShow"
      :key="post.id"
      :post="post"
    ></PostCard>
  </section>
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
