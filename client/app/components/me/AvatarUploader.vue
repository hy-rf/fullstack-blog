<script setup lang="ts">
import { ref } from "vue";

const file = ref<File | null>(null);
const message = ref<string>("");
const runtimeConfig = useRuntimeConfig();
const { t } = useI18n();

const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    file.value = target.files[0];
  }
};

const uploadAvatar = async () => {
  if (!file.value) {
    message.value = "Please select a file.";
    return;
  }

  const formData = new FormData();
  formData.append("file", file.value);
  const response = await fetch(
    `${runtimeConfig.public.GATEWAY_URL}/user/avatar`,
    {
      method: "post",
      body: formData,
      credentials: "include",
    },
  );
  if (response.ok) {
    alert(t("me.avatar_upload_success_message"));
    file.value = null;
  }
};
</script>

<template>
  <form @submit.prevent="uploadAvatar">
    <input type="file" @change="handleFileChange" >
    <button type="submit">Upload Avatar</button>
  </form>
</template>

<style lang="css" scoped></style>
