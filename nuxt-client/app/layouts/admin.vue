<script setup lang="ts">
const { t } = useI18n();
</script>

<template>
  <div>
    <h1 class="title">{{ t("admin.title") }}</h1>
    <ul>
      <li>
        <NuxtLink to="/admin">{{ t("admin.dashboard_link") }}</NuxtLink>
      </li>
      <li>
        <NuxtLink to="/admin/users">{{ t("admin.users_link") }}</NuxtLink>
      </li>
    </ul>
    <slot />
  </div>
</template>

<style lang="css" scoped>
ul {
  list-style: none;
  display: flex;
  gap: 1rem;
}
</style>
