<script setup lang="ts">
import type PostSummary from '~/types/PostSummary';

const { data, pending } = useFetch<PostSummary[]>("/api/saved-posts")

</script>

<template>
  <PostCard v-for="post in data" :post="post" />
  <div v-if="pending">Loading</div>
</template>

<style lang="css" scoped>

</style>