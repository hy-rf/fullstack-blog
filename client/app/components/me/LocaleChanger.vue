<script setup lang="ts">
const { locales, setLocale } = useI18n();
</script>

<template>
  <button
    v-for="locale in locales"
    :key="locale.code"
    @click="setLocale(locale.code)"
  >
    <p>{{ locale.name }}</p>
  </button>
</template>

<style lang="css" scoped></style>
