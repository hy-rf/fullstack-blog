<script setup lang="ts">
const userStore = useUserStore();
const { gtag } = useGtag();
const { t } = useI18n();
const router = useRouter();
const route = useRoute();

async function handleLogout() {
  // TODO: check permission if user current is in
  const needToRedirect = route.path.startsWith("/admin");
  await userStore.logout();
  if (needToRedirect) {
    router.push("/");
  }
}

function hasRole(role: string) {
  return !!(
    userStore.user &&
    Array.isArray(userStore.user.roles) &&
    userStore.user.roles.includes(role)
  );
}
</script>

<template>
  <button
    v-if="hasRole('ROLE_user')"
    class="logout-button"
    @click="handleLogout"
  >
    {{ t("nav.logout") }}
  </button>
</template>

<style lang="css" scoped>
.logout-button {
  /* height: 25px; */
  padding: 0 0.5rem;
  border: 0;
  background-color: transparent;
  cursor: pointer;
  color: black;
  font-size: 16px;
  font-family: "Hiragino Kaku Gothic ProN, sans-serif";

  &:hover {
    text-decoration: underline;
  }
}
</style>
