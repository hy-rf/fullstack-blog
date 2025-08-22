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
      alert("Reply submitted!");
      replyContent.value = "";
      props.showReplyForm.value = false;
      props.refreshReplies(); // Trigger a refresh of replies
      // Optionally, reload replies here
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
    <label for="reply-content" class="reply-label">Add a reply:</label>
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

<style lang="css" scoped></style>
