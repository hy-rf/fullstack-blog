<script setup lang="ts">
import AvatarUploader from "~/components/me/AvatarUploader.vue";
import UpdateProfileForm from "~/components/me/UpdateProfileForm.vue";
</script>

<template>
  <div id="update-main">
    <AvatarUploader />
    <UpdateProfileForm />
  </div>
</template>

<style lang="css" scoped>
#update-main {
  display: flex;
  flex-direction: column;
  gap: 1rem;
}
</style>
