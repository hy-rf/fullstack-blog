<script setup lang="ts">
const { t } = useI18n();
</script>

<template>
  <div style="height: 100%">
    <h1 class="title">{{ t("me.title") }}</h1>
    <slot />
  </div>
</template>

<style lang="css" scoped></style>
