<script setup lang="ts">
import type Reply from "~/types/Reply";
import ReplyCard from "./ReplyCard.vue";

const props = defineProps<{
  replies: Reply[];
}>();
</script>

<template>
  <ul>
    <ReplyCard v-for="r in props.replies" :key="r.id" :reply="r" />
    <!-- Display when No child replies for post -->
    <li v-if="!replies.length">No replies yet.</li>
  </ul>
</template>

<style scoped>
ul {
  list-style: none;
  margin-left: 0.5rem;
  padding-left: 0.5rem;
  border-left: 1px solid black;
}
li {
  padding: 0.65rem 1.5rem;
  font-size: 0.85rem;
}
</style>
