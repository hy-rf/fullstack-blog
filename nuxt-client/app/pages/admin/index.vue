<script setup lang="ts">
const { t } = useI18n();
</script>

<template>
  <li>
    <NuxtLink to="/admin/users">{{ t("admin.users_link") }}</NuxtLink>
  </li>
</template>

<style lang="css" scoped></style>
