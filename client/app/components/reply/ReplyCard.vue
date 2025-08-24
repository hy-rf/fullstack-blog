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
  <li :key="reply.id" class="reply-card">
    <div>
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
    <button
      v-if="userStore.user.roles.includes('ROLE_user')"
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
    <button @click="showReplies = !showReplies">
      {{ showReplies ? "hide" : "show" }}
    </button>
    <ReplyList
      v-if="showReplies && reply.replies"
      :key="refreshKey"
      :reply-id="reply.id.toString()"
      :replies="reply.replies"
    ></ReplyList>
  </li>
</template>

<style lang="css" scoped>
.reply-content {
  padding: 0.5rem;
  border: 1px solid gray;
  border-radius: 8px;
}
.created-time {
  text-align: end;
}
time {
  font-size: smaller;
  color: gray;
}
</style>
