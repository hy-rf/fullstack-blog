<script setup lang="ts">
const { t } = useI18n();
</script>

<template>
  <ul>
    <li>
      <NuxtLink to="/me/locale">
        <Icon name="circum:globe" size="20" style="vertical-align: sub" />
        {{ t("me.settings.locale") }}</NuxtLink
      >
    </li>
    <li>
      <AuthLogoutButton />
    </li>
  </ul>
</template>

<style lang="css" scoped>
li {
  padding-bottom: 0.5rem;
  margin-bottom: 0.5rem;
  border-bottom: 1px solid #cccccc;
}
a {
  color: black;
  text-decoration: none;
  display: inline-block;
  padding-left: 0.3rem;
}
</style>
