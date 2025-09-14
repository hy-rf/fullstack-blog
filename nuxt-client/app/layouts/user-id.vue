<script setup lang="ts">
import type UserBasicDto from "~/types/UserBasicDto";

const { t } = useI18n();

definePageMeta({
  validate: async (route) => {
    // Check if the id is made up of digits
    return typeof route.params.id === "string" && /^\d+$/.test(route.params.id);
  },
});

const route = useRoute();
const userId = route.params.id as string;

const { data, pending } = useFetch<UserBasicDto>(`/api/user/${userId}`);
</script>

<template>
  <div>
    <h1 class="title">User</h1>
    <div id="user-main" v-if="data">
      <div>
        <h2>{{ data.username }}</h2>
        <client-only>
          <span>{{ t("user.date_joined") }}</span>
          <time :datetime="data.createdAt">
            {{ new Date(data.createdAt).toLocaleDateString() }}
          </time>
        </client-only>
      </div>
      <div>
        <nuxt-img />
      </div>
    </div>
    <div v-if="pending">
      <p>Loading</p>
    </div>
    <slot />
  </div>
</template>

<style lang="css" scoped>
#user-main {
  display: flex;
}
</style>
