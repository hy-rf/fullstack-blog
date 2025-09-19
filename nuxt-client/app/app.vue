<script setup lang="ts">
const { gtag } = useGtag();
const userStore = useUserStore();
watch(
  () => userStore.loaded,
  () => {
    gtag("event", "screen_view", {
      app_name: "udevkit",
      screen_name: "Home",
    });
  },
);
</script>

<template>
  <MobileHeader />
  <DesktopHeader />
  <main>
    <NuxtLayout>
      <NuxtPage />
    </NuxtLayout>
  </main>
</template>

<style scoped>
main {
  margin-inline: auto;
  max-width: 768px;
  padding-block: 1rem 0;
  padding-inline: 1rem;
  min-height: calc(100dvh);
  box-shadow: 0 2px 16px rgba(35, 41, 70, 0.07);
  border-left: 1px solid #cccccc;
  border-right: 1px solid #cccccc;
}

@media screen and (min-width: 768px) {
  main {
    padding-top: 4.5rem;
  }
}
</style>

<style>
*,
*::before,
*::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}
textarea {
  resize: none;
}
html {
  background-color: #ffffff;
  /* This prevent horizontal scrolling on iOS */
  overflow-x: hidden;
  background: #eeeeee;
  overscroll-behavior-y: none;
}

::-webkit-scrollbar {
  display: none;
}

ul {
  list-style: none;
}
h1 {
  margin-top: 0;
}

@media screen and (min-width: 768px) {
  h1.title {
    display: none;
  }
}
</style>
