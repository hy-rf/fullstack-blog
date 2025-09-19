<script setup lang="ts">
import UserCard from "~/components/UserCard.vue";
import type UserBasicDto from "~/types/UserBasicDto";

const { t } = useI18n();
const config = useRuntimeConfig();

definePageMeta({
  validate: async (route) => {
    // Check if the id is made up of digits
    return typeof route.params.id === "string" && /^\d+$/.test(route.params.id);
  },
});

const route = useRoute();
const userId = route.params.id as string;

const { data, pending } = useFetch<UserBasicDto>(
  `${config.public.GATEWAY_URL}/user/${userId}`,
);
</script>

<template>
  <div>
    <h1 class="title">{{ t("user.title") }}</h1>
    <UserCard v-if="data" :user="data" />
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
