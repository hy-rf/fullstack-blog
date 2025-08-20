// ...existing code...
<script setup lang="ts">
import type Reply from "~/types/Reply";
import ReplyCard from "./ReplyCard.vue";

const props = defineProps<{ postId?: string; replyId?: string }>();

const replies = ref<Reply[]>([]);

async function load() {
  if (props.postId) {
    replies.value = await $fetch<Reply[]>(`/api/post/${props.postId}/replies`);
  }
  if (props.replyId) {
    replies.value = await $fetch<Reply[]>(
      `/api/reply/${props.replyId}/replies`
    );
  }
}

onMounted(load);
watch(() => props.postId, load);

</script>

<template>
  <ul>
    <ReplyCard v-for="r in replies" :key="r.id" :reply="r" />
    <!-- Display when No child replies for post -->
    <li v-if="!replies.length">No replies yet.</li>
  </ul>
</template>

<style scoped>
ul {
  list-style: none;
  margin: 0;
  padding: 0;
  border-left: 1px solid black;
}
li {
  padding: 0.65rem 1.5rem;
  font-size: 0.85rem;
}
</style>
