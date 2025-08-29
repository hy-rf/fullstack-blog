<script setup lang="ts">
import HeaderLink from "./HeaderLink.vue";

const { t, locales } = useI18n();
const userStore = useUserStore();
const route = useRoute();
</script>

<template>
  <header>
    <ul>
      <li :class="{ active: route.path == '/' }">
        <HeaderLink link="/">
          <Icon name="mdi-light:home" size="36" />
        </HeaderLink>
      </li>
      <li :class="{ active: route.path == '/post' }">
        <HeaderLink link="/post">
          <Icon name="mdi-light:magnify" size="36" />
        </HeaderLink>
      </li>
      <li :class="{ active: route.path == '/new' }">
        <HeaderLink link="/new">
          <Icon name="mdi-light:plus" size="55" />
        </HeaderLink>
      </li>
      <li :class="{ active: route.path == '/follow' }">
        <HeaderLink link="/follow">
          <Icon name="mdi-light:heart" size="36" />
        </HeaderLink>
      </li>
      <li :class="{ active: route.path == '/me' }">
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
  padding-inline: 0;
  background-color: #e9e9e9;
  filter: blur(1);
}
a {
  display: block;
}
a:active {
  color: #000;
}
ul {
  display: flex;
  width: 100%;
  list-style: none;
  height: 100%;
  li {
    width: 20%;
    padding-top: 0.5rem;
    text-align: center;
  }
  & > li:nth-child(3) {
    padding-top: 0;
    span {
      transform: translateY(-2px);
    }
  }
}
.active {
  background-color: #dddddd;
}
.icon {
  transition:
    transform 0.3s ease,
    color 0.3s ease;
}
</style>
