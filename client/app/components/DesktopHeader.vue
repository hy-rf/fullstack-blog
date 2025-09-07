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
          <Icon name="mdi-light:home" size="36" />
          <span>{{ t("nav.home") }}</span>
        </NuxtLink>
      </li>
      <li :class="{ active: route.path == '/search' }">
        <NuxtLink to="/search">
          <Icon name="mdi-light:magnify" size="36" />
          <span>{{ t("nav.search") }}</span>
        </NuxtLink>
      </li>
      <li :class="{ active: route.path == '/new' }">
        <NuxtLink to="/new">
          <Icon name="mdi-light:plus" size="36" />
          <span>{{ t("nav.new") }}</span>
        </NuxtLink>
      </li>
      <li :class="{ active: route.path == '/follow' }">
        <NuxtLink to="/follow">
          <Icon name="mdi-light:heart" size="36" />
          <span>{{ t("nav.follow") }}</span>
        </NuxtLink>
      </li>
      <li :class="{ active: route.path == '/me' }">
        <NuxtLink to="/me">
          <Icon name="mdi-light:account" size="36" />
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
  display: block;
  height: 100%;
  padding-block: 0.8rem;
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
    span {
      padding-left: 1rem;
      color: #000;
      display: inline-block;
      transform: translateY(-0.7rem);
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
