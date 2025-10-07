<script setup lang="ts">
const { t } = useI18n();
const config = useRuntimeConfig();

let data;
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
    {{ data.length }}
  </div>
</template>

<style lang="css" scoped></style>
