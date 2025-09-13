<script setup lang="ts">
import { ref } from "vue";
const username = ref("");
const password = ref("");
const { t } = useI18n();

const userStore = useUserStore();
const router = useRouter();

function routeAfterLoginSuccess() {
  if (router.options.history.state.back == "/register") {
    router.push("/");
  } else {
    router.go(-1);
  }
}

async function login() {
  const res = await fetch(`/api/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({
      username: username.value,
      password: password.value,
    }),
  });
  if (res.ok) {
    await userStore.fetchUser();
    alert(t("auth.login_success"));
    routeAfterLoginSuccess();
  } else {
    alert("Invalid credentials");
    username.value = "";
    password.value = "";
  }
}
</script>

<template>
  <div>
    <h1 class="title">Login</h1>
    <div class="login-container">
      <form @submit.prevent="login">
        <label>
          Username:
          <input v-model="username" type="text" required />
        </label>
        <label>
          Password:
          <input id="password" v-model="password" type="password" required />
        </label>
        <button type="submit">Login</button>
      </form>
      <nuxt-link to="/register">{{ t("nav.register") }}</nuxt-link>
    </div>
  </div>
</template>

<style scoped>
.login-container {
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
