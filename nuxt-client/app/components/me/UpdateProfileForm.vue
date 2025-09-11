<script setup lang="ts">
const { t } = useI18n();

const newUserName = ref("");
const newPassword = ref("");

async function updateMyProfile() {
  if (newUserName.value == "" || newPassword.value == "") {
    alert();
    return;
  }
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
    <label>
      <span>{{ t("me.update.username") }}</span>
      <input
        v-model="newUserName"
        type="text"
        placeholder="New Username"
        required
    /></label>
    <label>
      <span>{{ t("me.update.password") }}</span>
      <input
        v-model="newPassword"
        type="password"
        placeholder="New Password"
        required
    /></label>
    <button type="submit">
      {{ t("me.update.update_button") }}
    </button>
  </form>
</template>

<style lang="css" scoped>
form {
  display: flex;
  flex-direction: column;
  gap: 1rem;
  max-width: 500px;
  label {
    display: inline-flex;
    justify-content: space-between;
  }

  button {
    width: 10rem;
    align-self: self-end;
  }
}
</style>
