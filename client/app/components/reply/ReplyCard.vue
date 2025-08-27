<script setup lang="ts">
import type Reply from "~/types/Reply";

const { t, locale } = useI18n();
import { useUserStore } from "~/stores/user";
const userStore = useUserStore();
const props = defineProps({
  reply: {
    type: Object as () => Reply,
    required: true,
  },
});
const refreshKey = ref(0);
function refreshReplies() {
  refreshKey.value++;
}

const showReplyForm = ref(false);
const showReplies = ref(true);
const showReplyFormWrapper = computed(() => showReplyForm);
</script>

<template>
  <div class="reply-card">
    {{ t("reply.author") }}:
    <NuxtLink :to="`/user/${reply.author.id}`">
      {{ reply.author.username }}
    </NuxtLink>
  </div>
  <div class="reply-content">
    <p>{{ reply.content }}</p>
    <p class="created-time">
      <time>{{ new Date(reply.created).toLocaleString(locale) }}</time>
    </p>
  </div>
  <div class="create-reply">
    <button
      class="toggle-reply-form-button"
      v-if="userStore.isUser"
      @click="showReplyForm = !showReplyForm"
    >
      Reply
    </button>
    <ReplyEditor
      v-if="showReplyForm"
      :replyId="reply.id.toString()"
      :show-reply-form="showReplyFormWrapper"
      :refresh-replies="refreshReplies"
    />
  </div>
  <button class="toggle-reply-list-button" @click="showReplies = !showReplies">
    {{ showReplies ? "hide" : "show" }}
  </button>
  <ReplyList
    v-if="showReplies && reply.replies"
    :key="refreshKey"
    :reply-id="reply.id.toString()"
    :replies="reply.replies"
  ></ReplyList>
</template>

<style lang="css" scoped>
.reply-content {
  padding: 0.5rem;
  border: 1px solid #c8c8c8;
  border-radius: 0.5rem;
}
.reply-content:hover {
  border: 1px solid #808080;
}
.created-time {
  text-align: end;
}
time {
  font-size: smaller;
  color: gray;
}
.create-reply {
  display: flex;
  flex-direction: column;
  align-items: end;
}
</style>
