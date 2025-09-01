<script setup lang="ts">
const { t } = useI18n();
</script>

<template>
  <h1>{{ t("me.title") }}</h1>
  <slot />
</template>

<style lang="css" scoped></style>
