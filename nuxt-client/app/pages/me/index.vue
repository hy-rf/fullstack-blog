<script setup lang="ts">
const { t } = useI18n();
const userStore = useUserStore();
</script>

<template>
  <ul>
    <li>
      <div class="user-brief">
        <div>
          <div>
            <span>{{ t("me.username") }}</span>
            <span>{{ userStore.user.username }}</span>
          </div>
          <div v-if="userStore.isAdmin">
            <span class="role-list-title">{{ t("me.your_permissions") }}</span
            ><br />
            <span
              v-for="(r, i) in userStore.user.roles"
              :key="r"
              class="role-names"
            >
              {{ i + 1 + ": " + r.split("_")[1] + " " }} <br />
            </span>
          </div>
        </div>
        <img src="/favicon.ico" alt="" sizes="80 80" />
      </div>
    </li>
    <li>
      <NuxtLink to="/me/update">{{ t("me.update_link") }}</NuxtLink>
    </li>
    <li>
      <NuxtLink to="/me/saved-posts">{{ t("me.saved_posts_link") }}</NuxtLink>
    </li>
    <li>
      <NuxtLink to="/me/liked-posts">{{ t("me.liked_posts_link") }}</NuxtLink>
    </li>
    <li>
      <NuxtLink to="/me/settings">{{ t("me.settings_link") }}</NuxtLink>
    </li>
    <li v-if="userStore.isAdmin" style="margin-top: auto">
      <NuxtLink to="/admin">{{ t("me.admin_link") }}</NuxtLink>
    </li>
  </ul>
</template>

<style lang="css" scoped>
ul {
  display: flex;
  flex-direction: column;
  height: 100%;
  gap: 0.5rem;
}
li {
  padding-bottom: 0.4rem;
  border-bottom: 1px solid #cccccc;
}
.role-list-title {
  font-size: small;
}
.role-names {
  color: #aaaaaa;
  font-size: smaller;
}
a {
  color: black;
  text-decoration: none;
  display: block;
  padding-left: 0.3rem;
}
.user-brief {
  display: flex;
  img {
    margin-left: auto;
    width: 80px;
    height: 80px;
  }
}
</style>
