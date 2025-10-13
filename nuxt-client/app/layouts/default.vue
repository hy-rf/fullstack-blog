<script setup lang="ts">
import type { User } from "~/types/User";
import { useUserStore } from "~/stores/user";
const userStore = useUserStore();
const headers = useRequestHeaders(["cookie"]);
const cookie = useCookie("is-login");

const { data: user } = await useAsyncData<User>("user", async () => {
  if (!cookie.value) {
    return {
      username: "Guest",
      roles: [],
    };
  }
  try {
    const user: User = await $fetch<User>(
      import.meta.server ? "http://localhost:3000/api/me" : "/api/me",
      {
        credentials: "include",
        headers,
      },
    );
    cookie.value = "Y";
    return user;
  } catch {
    cookie.value = null;
    return {
      username: "Guest",
      roles: [],
    };
  }
});
// Init user and preferences(locale, color mode etc).
// TODO: manage start state of color mode and locale
userStore.init(user.value!);

if (import.meta.client) {
  console.log("Runs on client");
}
</script>

<template>
  <div style="position: relative; height: 100%">
    <slot />
  </div>
</template>

<style lang="css" scoped></style>
