<script setup lang="ts">
import type UserBasicDto from "~/types/UserBasicDto";

const config = useRuntimeConfig();

defineProps({
  user: {
    type: Object as () => UserBasicDto,
    required: true,
  },
});
</script>

<template>
  <div class="user-card-container">
    <div class="user-card-left">
      <p>{{ user.username }}</p>
      <time :datetime="user.createdAt">{{
        new Date(user.createdAt).toLocaleDateString()
      }}</time>
    </div>
    <div class="user-card-right">
      <img
        class="avatar-image"
        :src="`${config.public.FILES_PREFIX}${user.avatarUrls[user.avatarUrls.length - 1]}`"
        alt=""
      />
    </div>
  </div>
</template>

<style lang="css" scoped>
.user-card-container {
  display: flex;
  width: 100%;
  padding-inline: 0.5rem;
}
.user-card-left {
  width: 50%;
}
.user-card-right {
  max-width: 50%;
  display: flex;
  margin-left: auto;
}
.avatar-image {
  width: 120px;
  height: 120px;
  margin-bottom: 20%;
  border-radius: 50%;
}
</style>
