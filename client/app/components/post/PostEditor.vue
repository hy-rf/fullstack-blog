<script setup lang="ts">
import type PostToEditViewModel from "./PostToEditViewModel";

const { t } = useI18n();

const props = defineProps<{
  postToEdit: PostToEditViewModel;
}>();

const formRef = ref<HTMLFormElement | null>(null);

onMounted(() => {
  nextTick(() => {
    formRef.value?.classList.add("on");
  });
});

const postEditing = reactive({
  title: props.postToEdit.title,
  content: props.postToEdit.content,
});

const submitEditPost = async () => {
  console.log(postEditing);
};
</script>

<template>
  <form ref="formRef" @submit.prevent="submitEditPost">
    <input type="text" :value="postEditing.title" />
    <textarea name="" id="" :value="postEditing.content"></textarea>
    <button type="submit">{{ t("post.edit.submit_button") }}</button>
  </form>
</template>

<style lang="css" scoped>
form {
  position: fixed;
  background-color: #cccccc;
  display: flex;
  flex-direction: column;
  gap: 1rem;
  padding: 1rem 2rem;
  border-radius: 0.5rem;
  width: calc(100dvw - 4rem);
  height: 60dvh;
  margin: 0 2rem;
  left: 0;
  transform: translateY(-5px);
  opacity: 0;
}

.toolbox {
  display: inline-flex;
}

button {
  align-self: flex-end;
}

.close-button {
  margin-left: auto;
}

.on {
  transition: opacity 1s ease;
  transform: translateY(5px);
  opacity: 1;
}

input {
  border-radius: 0.5rem;
  outline: none;
  border: 0;
  padding: 0.3rem 0.6rem;
}

textarea {
  border-radius: 0.5rem;
  outline: none;
  border: 0;
  height: 5rem;
  padding: 0.3rem 0.6rem;
}
</style>
