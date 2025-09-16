<script setup lang="ts">
import { ref } from "vue";

const { t } = useI18n();
const username = ref("");
const password = ref("");
const message = ref("");
const router = useRouter();

const userStore = useUserStore();

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
      await fetch("/api/register", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({
          username: username.value,
          password: password.value,
        }),
      });
      await userStore.fetchUser();
      router.push("/me");
    } else {
      message.value = text;
    }
  } catch (err) {
    message.value = `${err}`;
  }
};
</script>

<template>
  <div>
    <h1 class="title">Register</h1>
    <div class="register-container">
      <form autocomplete="off" @submit.prevent="register">
        <label for="username"
          >Username:
          <input
            id="username"
            v-model="username"
            type="text"
            required
            autocomplete="off"
          />
        </label>

        <label for="password"
          >Password:
          <input
            id="password"
            v-model="password"
            type="password"
            required
            autocomplete="off"
          />
        </label>
        <div class="bottom-toolbar">
          <p>
            <span>{{ t("auth.register.already_have_account") }}</span>
            <nuxt-link to="/login">{{ t("nav.login") }}</nuxt-link>
          </p>
          <button type="submit">{{ t("auth.register.submit_button") }}</button>
        </div>
      </form>
    </div>
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
  border: 1px solid #aaaaaa;
  border-radius: 16px;
  padding: 0.5rem 1rem;
  max-width: 5rem;
  align-self: end;
}
.bottom-toolbar {
  display: flex;
  width: 100%;
  gap: 1rem;
  justify-content: space-around;
  p {
    padding-top: 0.5rem;
    a {
      text-decoration: none;
      display: inline-block;
      border: 1px solid #aaaaaa;
      padding: 0.5rem 1rem;
      margin-left: 1rem;
      border-radius: 16px;
    }
    a:visited {
      color: #000;
    }
  }
}
</style>
