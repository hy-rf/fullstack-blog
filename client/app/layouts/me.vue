<script setup lang="ts">
const { t } = useI18n();
</script>

<template>
  <div>
    <h1>{{ t("me.title") }}</h1>
    <slot />
  </div>
</template>

<style lang="css" scoped></style>
