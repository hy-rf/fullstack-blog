<script setup lang="ts">
import { useUserStore } from "./stores/user";
import type { User } from "./types/User";
const userStore = useUserStore();
const { gtag } = useGtag();
const { t } = useI18n();
const headers = useRequestHeaders(["cookie"]);

const username = computed(() => {
  return (
    (userStore.isUser && userStore.user.username) ||
    t("auth.username_guest") ||
    "Guest"
  );
});

function hasRole(role: string) {
  return !!(
    userStore.user &&
    Array.isArray(userStore.user.roles) &&
    userStore.user.roles.includes(role)
  );
}

const { data: user } = await useAsyncData<User>("user", async () => {
  try {
    return await $fetch<User>(
      import.meta.server ? "http://localhost:3000/api/me" : "/api/me",
      {
        credentials: "include",
        headers,
      }
    );
  } catch {
    return {
      username: "Guest",
      roles: [],
    };
  }
});

userStore.init(user.value!);
console.log("Client user initialized!");
console.table(user.value);

watch(
  () => userStore.loaded,
  () => {
    gtag("event", "screen_view", {
      app_name: "udevkit",
      screen_name: "Home",
    });
    console.table(userStore.$state);
  }
);
const runtimeConfig = useRuntimeConfig();
</script>

<template>
  <MobileHeader />
  <main>
    <NuxtPage />
  </main>
</template>

<style scoped>
main {
  margin-inline: auto;
  max-width: 768px;
  padding-block: 1rem 0;
  padding-inline: 1rem;
  background: #eeeeee;
  min-height: calc(100dvh - 50px);
  border-radius: 12px;
  box-shadow: 0 2px 16px rgba(35, 41, 70, 0.07);
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
}
h1 {
  margin-top: 0;
}
</style>
