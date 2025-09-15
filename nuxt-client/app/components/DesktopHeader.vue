<script setup lang="ts">
const { t } = useI18n();
const route = useRoute();

const headerOpaque = ref(false);
</script>

<template>
  <header
    class="site-header"
    :class="headerOpaque ? 'header-hide' : ''"
    aria-hidden="false"
  >
    <ul>
      <li :class="{ active: route.path == '/' }">
        <NuxtLink to="/">
          <Icon name="material-symbols:house-outline" size="36" />
          <span>{{ t("nav.home") }}</span>
        </NuxtLink>
      </li>
      <li :class="{ active: route.path == '/search' }">
        <NuxtLink to="/search">
          <Icon name="material-symbols:search" size="36" />
          <span>{{ t("nav.search") }}</span>
        </NuxtLink>
      </li>
      <li :class="{ active: route.path == '/new' }">
        <NuxtLink to="/new">
          <Icon name="material-symbols:add-2" size="36" />
          <span>{{ t("nav.new") }}</span>
        </NuxtLink>
      </li>
      <li :class="{ active: route.path == '/follow' }">
        <NuxtLink to="/follow">
          <Icon name="material-symbols:favorite-outline" size="36" />
          <span>{{ t("nav.follow") }}</span>
        </NuxtLink>
      </li>
      <li :class="{ active: route.path == '/me' }">
        <NuxtLink to="/me">
          <Icon name="material-symbols:account-circle-outline" size="36" />
          <span>{{ t("nav.me") }}</span>
        </NuxtLink>
      </li>
    </ul>
  </header>
</template>

<style lang="css" scoped>
header {
  position: fixed;
  display: flex;
  width: 100vw;
  margin-inline: auto;
  top: 0;
  height: 4rem;
  background-color: rgba(233, 233, 233, 0.5);
  backdrop-filter: blur(1px);
  opacity: 1;
  z-index: 99;
}
a {
  display: flex;
  height: 100%;
  padding-block: 0.8rem;
  align-content: space-between;
  text-decoration: none;
}
a:active {
  color: #000;
}
ul {
  display: flex;
  height: 100%;
  list-style: none;
  width: 768px;
  margin-inline: auto;
  li {
    width: 20%;
    text-align: center;
    padding-inline: 0.5rem;

    span {
      padding-top: 0.5rem;
      margin-inline: auto;
      color: #000;
      display: inline-block;
      transform: translateY(0);
      vertical-align: super;
    }
  }
}
.active {
  background-color: #ddddddee;
}

@media screen and (max-width: 767px) {
  header {
    display: none;
  }
}
</style>
