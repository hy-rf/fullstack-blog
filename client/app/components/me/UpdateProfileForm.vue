<script setup lang="ts">
const newUserName = ref("");
const newPassword = ref("");

async function updateMyProfile() {
  const response = await fetch("/api/user", {
    method: "PUT",
    headers: {
      "Content-Type": "application/json",
    },
    body: JSON.stringify({
      username: newUserName.value,
      password: newPassword.value,
    }),
    credentials: "include",
  });

  if (!response.ok) {
    console.error("Failed to update profile");
  } else {
    console.log("Profile updated successfully");
  }
}
</script>

<template>
  <form @submit.prevent="updateMyProfile">
    <input
      v-model="newUserName"
      type="text"
      placeholder="New Username"
      required
    >
    <input
      v-model="newPassword"
      type="password"
      placeholder="New Password"
      required
    >
    <button type="submit">Update Profile</button>
  </form>
</template>

<style lang="css" scoped></style>
