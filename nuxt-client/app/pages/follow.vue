<script setup lang="ts">
import type UserBasicDto from "~/types/UserBasicDto";

const { t } = useI18n();
const config = useRuntimeConfig();

let data: UserBasicDto[];
if (import.meta.server) {
  const headers = useRequestHeaders(["cookie"]);
  data = await $fetch(`${config.URL}/following`, {
    credentials: "include",
    headers,
  });
} else {
  data = await $fetch(`${config.public.GATEWAY_URL}/following`, {
    credentials: "include",
  });
}
</script>

<template>
  <div>
    <h1 class="title">{{ t("follow.title") }}</h1>
    <div>follow</div>
    <div id="following-list">
      <user-card v-for="user in data" :key="user.id" :user="user" />
    </div>
  </div>
</template>

<style lang="css" scoped></style>
