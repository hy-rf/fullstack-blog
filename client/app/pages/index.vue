<script setup lang="ts">
import PostCard from "~/components/post/PostCard.vue";
import type PostSummary from "~/types/PostSummary";

const DURATION_OF_POSTCARD_ANIMATION = 500;

const { t, locale } = useI18n();
const route = useRoute();
const router = useRouter();

const homePostStore = useHomePostStore();

homePostStore.setFromRoute(route.query);

const { data: posts, pending } = useAsyncData<Array<PostSummary>>("post", () =>
  $fetch(`/api/post?page_token=1`)
);

const isAnimating = ref(false);
const skipNext = ref(false);
let removeGuard: (() => void) | undefined;

function idFromTo(to: any) {
  return (to.params && to.params.id) || (to.query && to.query.id) || null;
}

async function startPostCardAnimationFor(to: any) {
  if (isAnimating.value) return;
  isAnimating.value = true;

  const id = idFromTo(to);
  const el: HTMLElement =
    (id && document.getElementById(`post-card-${id}`)) ||
    document.querySelector(".post-list");
  const pos = el.getBoundingClientRect();
  const postCardFinishStateStyle = {
    transform: `translateY(calc(-${pos.top}px + 1.5rem))`,
    border: "1px solid #ffffff",
  };
  const titleFinishStateStyle = {
    opacity: 0,
  };

  const titleElement: HTMLElement | null =
    document.getElementById("title") || null;
  if (titleElement) {
    titleElement.animate(
      [
        {
          opacity: 1,
        },
        titleFinishStateStyle,
      ],
      { duration: DURATION_OF_POSTCARD_ANIMATION, easing: "linear" }
    );
  }

  if (el) {
    const anim = el.animate(
      [
        { transform: "translateY(0)", border: "1px solid #e0e0e0" },
        postCardFinishStateStyle,
      ],
      { duration: DURATION_OF_POSTCARD_ANIMATION, easing: "linear" }
    );

    try {
      await anim.finished;
      Object.assign(titleElement?.style!, titleFinishStateStyle);
      Object.assign(el.style, postCardFinishStateStyle);
    } catch {}
  }
  skipNext.value = true;
  isAnimating.value = false;
  await router.push(to.fullPath);
}

onMounted(() => {
  removeGuard = router.beforeEach((to) => {
    if (skipNext.value) {
      skipNext.value = false;
      return true;
    }
    if (to.path && to.path.startsWith("/post")) {
      startPostCardAnimationFor(to);
      return false;
    }

    return true;
  });
});

onBeforeUnmount(() => {
  if (removeGuard) removeGuard();
});
</script>

<template>
  <h1 id="title">{{ t("home.feed") }}</h1>
  <section class="post-list" aria-label="Posts list">
    <PostCard
      v-for="post in posts"
      :key="post.id"
      :post="post"
      :id="'post-card-' + post.id"
    ></PostCard>
  </section>
  <div v-if="pending">
    <p>Loading...</p>
  </div>
</template>

<style scoped>
.post-list {
  padding-top: 0.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;
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
