<script setup lang="ts">
// TODO: save scroll info so that can keep after browsing other routes then back
import PostCard from "~/components/post/PostCard.vue";
import type PostSummary from "~/types/PostSummary";
const postStore = useHomePostsStore();
const { t, locale } = useI18n();

const { data: posts, pending } = useFetch<PostSummary[]>(
  `/api/post?offset=${postStore.offset || 0}`,
);

const postsToShow = computed(() => {
  if (postStore.posts && postStore.posts.length > 0) {
    return postStore.posts;
  }
  return posts.value;
});

const isFetchingMore = ref(false);
const threshold = 3000;

const loadMorePosts = async () => {
  if (isFetchingMore.value) return;
  isFetchingMore.value = true;
  postStore.offset += 50;
  try {
    const data = await fetch(`/api/post?offset=${postStore.offset}`).then((r) =>
      r.json(),
    );
    postStore.append(data);
  } finally {
    isFetchingMore.value = false;
  }
};
const listRef = ref<HTMLElement | null>(null);
const onScroll = () => {
  const el = listRef.value;
  // should not fetch while fetching
  if (isFetchingMore.value || !el) return;
  const { scrollHeight } = el;
  if (window.scrollY >= scrollHeight - threshold) {
    loadMorePosts();
  }
};

onMounted(async () => {
  postStore.posts = postsToShow.value!; // postStore.posts is [] onBeforeUnmount without this line when the page was ssr not in csr though,
  // which may cause state of browsing not being saved if no loadMorePosts called at least 1 time
  document.addEventListener("scroll", onScroll);

  const options: ScrollToOptions = {
    top: postStore.scrollY,
  };
  // Don't know why does setTimeout need here
  // And it only work if time >?0, 100 is safe
  // what does 'work' mean is that scroll position should be kept when navigating back from other routes that this home page was originally both ssr and csr
  setTimeout(() => {
    window.scrollTo(options);
  }, 100);
});

onBeforeUnmount(() => {
  document.removeEventListener("scroll", onScroll);
  postStore.scrollY = window.scrollY;
});
</script>

<template>
  <h1>{{ t("home.feed") }}</h1>
  <section ref="listRef" class="post-list" aria-label="Posts list">
    <PostCard
      v-for="post in postStore.posts || posts"
      :key="post.id"
      :post="post"
      hydrate-on-visible
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
