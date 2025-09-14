<script setup lang="ts">
import { ref } from "vue";

const file = ref<File | null>(null);
const runtimeConfig = useRuntimeConfig();
const { t } = useI18n();
const userStore = useUserStore();

const handleFileChange = (event: Event) => {
  const target = event.target as HTMLInputElement;
  if (target.files && target.files.length > 0) {
    file.value = target.files[0];
  }
};

const uploadAvatar = async () => {
  if (!file.value) {
    alert(t("me.update.avatar.null_error"));
    file.value = null;
    return;
  }
  if (!file.value.type.startsWith("image")) {
    alert(t("me.update.avatar.type_error"));
    file.value = null;
    return;
  }
  if (file.value.size > 1024 * 1000) {
    alert(t("me.update.avatar.size_error"));
    file.value = null;
    return;
  }
  const fileToUpload: File = new File(
    [file.value],
    `${userStore.user.username}_${file.value.lastModified.toString()}`,
    { type: file.value.type },
  );

  // Start to upload
  const formData = new FormData();
  formData.append("file", fileToUpload);
  const response = await fetch(
    `${runtimeConfig.public.GATEWAY_URL}/user/avatar`,
    {
      method: "post",
      body: formData,
      credentials: "include",
    },
  );
  if (response.ok) {
    alert(t("me.update.avatar.success_message"));
    file.value = null;
  } else {
    alert(t("me.update.avatar.error"));
    file.value = null;
  }
};
</script>

<template>
  <form @submit.prevent="uploadAvatar">
    <label class="file-input">
      <input
        type="file"
        :aria-label="t('me.update.choose_file_label')"
        accept="image/*"
        @change="handleFileChange"
      />
      <span class="file-label">{{ t("me.update.choose_file_label") }}</span>
    </label>
    <image-cropper :image="file!" />
    <button type="submit">{{ t("me.update.update_avatar_button") }}</button>
  </form>
</template>

<style lang="css" scoped>
form {
  display: inline-flex;
  flex-direction: column;
  & > button:nth-child(2) {
    max-width: fit-content;
    align-self: self-end;
  }
}
.file-input input[type="file"] {
  position: relative;
  inset: 0;
  width: 100%;
  height: 1rem;
  opacity: 0;
  cursor: pointer;
}
button {
  width: 10rem;
  margin-left: auto;
}
</style>
