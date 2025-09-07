<script setup lang="ts">
const route = useRoute();

const headerOpaque = ref(false);
// let rafId: number | null = null;
// const lastScrollY = ref(0);

// function onScroll() {
//   if (rafId) cancelAnimationFrame(rafId);
//   rafId = requestAnimationFrame(() => {
//     const currentY = window.scrollY || 0;
//     const delta = currentY - lastScrollY.value;
//     const threshold = 5;

//     if (currentY <= 10) {
//       headerOpaque.value = false;
//     } else if (delta > threshold) {
//       headerOpaque.value = true;
//     } else if (delta < -threshold) {
//       headerOpaque.value = false;
//     }

//     lastScrollY.value = currentY;
//   });
// }

// onMounted(() => {
//   window.addEventListener("scroll", onScroll, { passive: true });
//   onScroll();
// });

// onUnmounted(() => {
//   window.removeEventListener("scroll", onScroll);
//   if (rafId) cancelAnimationFrame(rafId);
// });
</script>

<template>
  <header
    class="site-header"
    :class="headerOpaque ? 'header-hide' : ''"
    aria-hidden="false"
  >
    <ul>
      <li :class="{ active: route.path == '/' }">
        <NuxtLink to="/">
          <Icon name="mdi-light:home" size="36" />
        </NuxtLink>
      </li>
      <li :class="{ active: route.path == '/search' }">
        <NuxtLink to="/search">
          <Icon name="mdi-light:magnify" size="36" />
        </NuxtLink>
      </li>
      <li :class="{ active: route.path == '/new' }">
        <NuxtLink to="/new">
          <Icon name="mdi-light:plus" size="36" />
        </NuxtLink>
      </li>
      <li :class="{ active: route.path == '/follow' }">
        <NuxtLink to="/follow">
          <Icon name="mdi-light:heart" size="36" />
        </NuxtLink>
      </li>
      <li :class="{ active: route.path == '/me' }">
        <NuxtLink to="/me">
          <Icon name="mdi-light:account" size="36" />
        </NuxtLink>
      </li>
    </ul>
  </header>
</template>

<style lang="css" scoped>
header {
  position: fixed;
  bottom: 0 !important;
  height: 50px;
  width: 100vw;
  padding-inline: 0;
  background-color: rgba(233, 233, 233, 0.5);
  backdrop-filter: blur(1px);
  opacity: 1;
  transition:
    opacity 400ms ease,
    backdrop-filter 1s ease;
  z-index: 99;
}
a {
  display: block;
  padding-top: 0.5rem;
  height: 100%;
}
a:active {
  color: #000;
}
ul {
  display: flex;
  width: 100%;
  list-style: none;
  height: 100%;
  li {
    width: 20%;
    text-align: center;
    span {
      color: #000;
    }
  }
}
.active {
  background-color: #ddddddee;
}
.icon {
  transition:
    transform 0.3s ease,
    color 0.3s ease;
}
.header-hide {
  opacity: 0.3;
  backdrop-filter: blur(1px);
  transition:
    opacity 400ms ease,
    backdrop-filter 1s ease;
}

@media screen and (min-width: 768px) {
  header {
    display: none;
  }
}
</style>
