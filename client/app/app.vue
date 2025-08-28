<script setup lang="ts">
import { useUserStore } from "./stores/user";
import LogoutButton from "./components/auth/LogoutButton.vue";
import type { User } from "./types/User";
const { locales, setLocale } = useI18n();
const userStore = useUserStore();
const { gtag } = useGtag();
const { t } = useI18n();
const headers = useRequestHeaders(["cookie"]);

const username = computed(() => {
  return (
    (userStore.isUser && userStore.user.username) ||
    t("auth.username_guest") ||
    "Guest"
  );
});

function hasRole(role: string) {
  return !!(
    userStore.user &&
    Array.isArray(userStore.user.roles) &&
    userStore.user.roles.includes(role)
  );
}

const { data: user } = await useAsyncData<User>("user", async () => {
  try {
    return await $fetch<User>(
      import.meta.server ? "http://localhost:3000/api/me" : "/api/me",
      {
        credentials: "include",
        headers,
      }
    );
  } catch {
    return {
      username: "Guest",
      roles: [],
    };
  }
});

userStore.init(user.value!);
console.log("Client user initialized!");
console.table(user.value);

watch(
  () => userStore.loaded,
  () => {
    gtag("event", "screen_view", {
      app_name: "udevkit",
      screen_name: "Home",
    });
    console.table(userStore.$state);
  }
);
const runtimeConfig = useRuntimeConfig();
</script>

<template>
  <header>
    <nav>
      <div class="nav-left">
        <ul class="nav-links">
          <li>
            <NuxtLink to="/">{{ t("nav.home") }}</NuxtLink>
          </li>
          <li>
            <NuxtLink to="/post">{{ t("nav.search") }}</NuxtLink>
          </li>
          <li v-if="userStore.isAdmin">
            <NuxtLink to="/admin/users">{{ t("nav.users") }}</NuxtLink>
          </li>
          <li v-if="userStore.isUser">
            <NuxtLink to="/new">{{ t("nav.new") }}</NuxtLink>
          </li>

          <li v-if="userStore.isUser">
            <NuxtLink to="/me">{{ t("nav.me") }}</NuxtLink>
          </li>
        </ul>
      </div>
      <div class="nav-right">
        <div class="locale-dropdown">
          <button class="locale-button">🌐</button>
          <div class="locale-menu">
            <button
              v-for="locale in locales"
              :key="locale.code"
              :class="{ active: $i18n.locale === locale.code }"
              @click="setLocale(locale.code)"
            >
              {{ locale.name }}
            </button>
          </div>
        </div>
        <div class="user-info">
          <div class="user-avatar">
            <span>👤 {{ username }}</span>
          </div>
          <div class="auth-links">
            <ul v-if="!userStore.isUser" class="login-and-register">
              <li>
                <NuxtLink to="/login">{{ t("nav.login") }}</NuxtLink>
              </li>
              <li>
                <NuxtLink to="/register">{{ t("nav.register") }}</NuxtLink>
              </li>
            </ul>
            <LogoutButton />
          </div>
        </div>
      </div>
    </nav>
  </header>
  <main class="main-content">
    <NuxtPage />
  </main>
  <footer>Version: {{ runtimeConfig.public.VERSION }}</footer>
</template>

<style scoped>
header {
  position: sticky;
  top: 0;
  z-index: 100;
  background: #c6f7ff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.07);
  height: 70px;
}

nav {
  display: flex;
  height: 100%;
  justify-content: space-between;
  align-items: center;
  margin: 0 auto;
  padding: 0.5rem 2rem;
}

.logo {
  font-weight: bold;
  font-size: 1.3rem;
  color: #eebbc3;
  text-decoration: none;
  margin-right: 2rem;
  letter-spacing: 1px;
  transition: color 0.2s;
}
.logo:hover {
  color: #ffd803;
}

.nav-links {
  display: flex;
  gap: 1.2rem;
  list-style: none;
  margin: 0;
  padding: 0;
}
.nav-links li {
  display: flex;
}
.nav-links a {
  text-decoration: none;
  padding: 0.3rem 0.7rem;
  border-radius: 4px;
}
.nav-links a:hover,
.nav-links .router-link-exact-active {
  text-decoration: underline;
}

.nav-right {
  display: flex;
  align-items: center;
  gap: 1.5rem;
}

.locale-dropdown {
  position: relative;
}

.locale-button {
  background: transparent;
  border: 0;
  color: #ffffff;
  padding: 0.2rem 0.7rem;
  border-radius: 4px;
  cursor: pointer;
  font-size: 0.95rem;
}

.locale-menu {
  position: absolute;
  /* top: 110%; */
  right: 0;
  display: none;
  flex-direction: column;
  background: #a2aca1;
  border: 0;
  border-radius: 8px;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  z-index: 10;
}

.locale-menu button {
  background: transparent;
  border: none;
  padding: 0.5rem 1rem;
  text-align: left;
  font-size: 0.95rem;
  cursor: pointer;
  color: #000000;
}

.locale-menu button:hover,
.locale-menu button.active {
  color: #ffffff;
}

/* Show menu on hover */
.locale-dropdown:hover .locale-menu {
  display: flex;
}

.user-info {
  font-size: 1rem;
  color: #ff0000;
  margin-left: 0.5rem;
  flex-direction: row;
  display: flex;
}

.loading {
  color: #aaa;
  font-size: 0.95rem;
}

.main-content {
  max-width: 900px;
  margin: 2rem auto 0 auto;
  padding: 2rem 1rem;
  background: #f4f6fb;
  min-height: 80vh;
  border-radius: 12px;
  box-shadow: 0 2px 16px rgba(35, 41, 70, 0.07);
}

.login-and-register {
  display: flex;
  gap: 1rem;
  list-style: none;
  & > li {
    width: 4rem;
    text-align: center;
  }
}

.user-avatar {
  width: 5rem;
}

@media (min-width: 768px) {
}
</style>

<style>
*,
*::before,
*::after {
  box-sizing: border-box;
  margin: 0;
  padding: 0;
}

a {
  color: black;
  text-decoration: none;
  &:hover {
    text-decoration: underline;
  }
}
h1 {
  font-size: xx-large;
}
h2 {
  font-size: larger;
}
button {
  padding-block: 0.4rem;
  padding-inline: 0.8rem;
}
</style>
