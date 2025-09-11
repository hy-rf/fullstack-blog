<script setup lang="ts">
import { ref } from "vue";

const file = ref<File | null>(null);
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
    alert(t("me.update.avatar.error"));
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
    <label class="file-input">
      <input
        type="file"
        :aria-label="t('me.update.choose_file_label')"
        accept="image/*"
        @change="handleFileChange"
      />
      <span class="file-label">{{ t("me.update.choose_file_label") }}</span>
    </label>
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
</style>
