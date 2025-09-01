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
          <span class="role-list-title">Your permissions: </span><br />
          <span class="role-names" v-for="(r, i) in userStore.user.roles">
            {{ i + 1 + ": " + r.split("_")[1] + " " }} <br />
          </span>
        </div>
        <img src="/favicon.ico" alt="" sizes="80 80" />
      </div>
    </li>
    <li>
      <NuxtLink to="/me/update">Update Account</NuxtLink>
    </li>
    <li>
      <NuxtLink to="/me/settigs">Settings</NuxtLink>
    </li>
  </ul>
</template>

<style lang="css" scoped>
ul {
  list-style: none;
}
li {
  padding-bottom: 0.5rem;
  margin-bottom: 0.5rem;
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
  display: inline-block;
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
