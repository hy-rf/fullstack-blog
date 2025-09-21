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
        autocomplete="off"
    /></label>
    <label>
      <span>{{ t("me.update.password") }}</span>
      <input
        v-model="newPassword"
        type="password"
        placeholder="New Password"
        required
        autocomplete="off"
    /></label>
    <button type="submit">
      {{ t("me.update.update_button") }}
    </button>
  </form>
</template>

<style lang="css" scoped>
form {
  border-radius: 0.5rem;
  background-color: #dddddd;
  padding: 0.5rem;
  display: flex;
  flex-direction: column;
  gap: 1rem;

  label {
    display: inline-flex;
    justify-content: space-between;
    span {
      display: inline-block;
      padding-top: 0.8rem;
      line-height: 100%;
    }
    input {
      border-radius: 0.3rem;
      padding: 0.5rem;
      max-width: 10rem;
      height: 100%;
      z-index: 1;
      position: relative;
      border: 1px solid grey;
      background-color: white;
      outline: none;
    }
  }

  button {
    align-self: self-end;
    border-radius: 0.5rem;
    border: 1px solid grey;
    padding: 0.5rem 0.5rem;
  }
}
</style>
