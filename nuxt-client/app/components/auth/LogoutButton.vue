<script setup lang="ts">
const userStore = useUserStore();
const { t } = useI18n();
const router = useRouter();
const route = useRoute();

async function handleLogout() {
  await userStore.logout();
  if (route.path.startsWith("/me")) {
    router.push("/");
  }
  if (route.path.startsWith("/admin")) {
    router.push("/");
  }
}
</script>

<template>
  <a
    v-if="userStore.isUser"
    class="logout-button"
    @click.prevent="handleLogout"
  >
    <Icon
      name="material-symbols-light:logout-rounded"
      size="20"
      style="vertical-align: sub"
    />
    {{ t("nav.logout") }}
  </a>
</template>

<style lang="css" scoped>
.logout-button {
  /* height: 25px; */
  padding-left: 0.3rem;
  border: 0;
  background-color: transparent;
  cursor: pointer;
  color: black;
  font-size: 16px;
  font-family: "Hiragino Kaku Gothic ProN, sans-serif";

  &:hover {
    text-decoration: none;
  }
}
</style>
