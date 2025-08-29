<script setup lang="ts">
import HeaderLink from "./HeaderLink.vue";

const { t, locales } = useI18n();
const userStore = useUserStore();
const route = useRoute();
</script>

<template>
  <header>
    <ul>
      <li>
        <HeaderLink link="/">
          <Icon name="mdi-light:home" size="36" />
        </HeaderLink>
      </li>
      <li>
        <HeaderLink link="/post">
          <Icon name="mdi-light:magnify" size="36" />
        </HeaderLink>
      </li>
      <li>
        <HeaderLink link="/new">
          <Icon name="line-md:plus" size="36" />
        </HeaderLink>
      </li>
      <li>
        <HeaderLink link="/me">
          <Icon name="mdi-light:bookmark" size="36" />
        </HeaderLink>
      </li>
      <li>
        <HeaderLink link="/me">
          <Icon name="mdi-light:account" size="36" />
        </HeaderLink>
      </li>
    </ul>
  </header>
</template>

<style lang="css" scoped>
header {
  position: fixed;
  bottom: 0;
  height: 50px;
  width: 100vw;
  padding-inline: 0.5rem;
}
ul {
  display: flex;
  width: 100%;
  list-style: none;
  height: 100%;
  padding-top: 0.5rem;
  li {
    width: 20%;
    text-align: center;
  }
}
li .nuxt-link-exact-active {
  background-color: #e0e0e0;
}
.icon {
  transition:
    transform 0.3s ease,
    color 0.3s ease;
}
</style>
