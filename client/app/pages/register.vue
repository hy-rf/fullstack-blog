<script setup lang="ts">
import { ref } from "vue";
import { useRouter } from "vue-router";

const username = ref("");
const password = ref("");
const message = ref("");
const router = useRouter();

const register = async () => {
  message.value = "";
  try {
    const res = await fetch("/api/register", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({
        username: username.value,
        password: password.value,
      }),
    });
    const text = await res.text();
    if (res.ok) {
      message.value = text;
    } else {
      message.value = text;
    }
  } catch (err) {
    message.value = "Network error";
  }
};
</script>

<template>
  <div class="register-container">
    <h1>Register</h1>
    <form @submit.prevent="register" autocomplete="off">
      <label for="username"
        >Username: <input
          type="text"
          id="username"
          v-model="username"
          required
          autocomplete="off"
        />
      </label>

      <label for="password"
        >Password: <input
          type="password"
          id="password"
          v-model="password"
          required
          autocomplete="off"
        />
      </label>

      <button type="submit">Register</button>
    </form>
  </div>
</template>

<style lang="css" scoped>
.register-container {
  max-width: 400px;
  margin: 0 auto;
  padding: 2rem;
  border: 1px solid #eee;
  border-radius: 8px;
}
form {
  display: flex;
  flex-direction: column;
  align-items: center;
}
label {
  display: block;
  margin-bottom: 1rem;
}
button {
  border: 1px solid gray;
  border-radius: 16px;
  padding: 0.5rem 1rem;
  max-width: 5rem;
  align-self: end;
}
</style>
