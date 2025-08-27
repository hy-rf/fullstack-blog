<script setup lang="ts">
const props = defineProps({
  replyId: {
    type: String,
    required: true,
  },
  showReplyForm: {
    type: Object as () => Ref<boolean>,
    required: true,
  },
  refreshReplies: {
    type: Object as () => () => void,
    required: true,
  },
});

const route = useRoute();
const router = useRouter();
const postId = route.params.id as string;

const replyContent = ref("");
const replyMessage = ref("");

const submitReply = async () => {
  replyMessage.value = "";
  try {
    const res = await fetch("/api/reply", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        postId: postId,
        parentReplyId: props.replyId,
        content: replyContent.value,
        // parentReplyId can be added here if replying to another reply
      }),
    });
    if (res.ok) {
      console.log("Reply submitted!");
      replyContent.value = "";
      props.showReplyForm.value = false;
      props.refreshReplies();
      router.replace(route.fullPath);
    } else {
      replyMessage.value = await res.text();
    }
  } catch (e) {
    replyMessage.value = "Network error";
  }
};
</script>

<template>
  <form class="reply-form" @submit.prevent="submitReply">
    <textarea
      id="reply-content"
      v-model="replyContent"
      class="reply-textarea"
      rows="3"
      required
    ></textarea>
    <button type="submit" class="reply-btn">Submit</button>
    <div v-if="replyMessage" class="reply-message">{{ replyMessage }}</div>
  </form>
</template>

<style lang="css" scoped>
.reply-form {
  display: flex;
  align-items: end;
  gap: 0.5rem;
}
#reply-content {
  resize: none;
  padding: 0.5rem;
  border-radius: 0.5rem;
  border: 1px solid #c8c8c8;
  transition: border 0.3s ease;
}
#reply-content:focus {
  border: 1px solid #808080;
  outline: none;
}
button {
  height: 2rem;
  padding-inline: 1rem;
}
</style>
