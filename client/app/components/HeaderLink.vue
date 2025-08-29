<script setup lang="ts">
defineProps({
  link: {
    type: String,
    required: true,
  },
});
</script>

<template>
  <NuxtLink :to="link">
    <slot></slot>
  </NuxtLink>
</template>

<style lang="css" scoped></style>
