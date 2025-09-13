<script setup lang="ts">
import type { User } from "~/types/User";

const { t } = useI18n();
definePageMeta({
  validate: async (route) => {
    // Check if the id is made up of digits
    return typeof route.params.id === "string" && /^\d+$/.test(route.params.id);
  },
});

const route = useRoute();
const userId = route.params.id as string;

const { data } = useFetch<User>(`/api/user/${userId}`);
</script>

<template>
  <div>
    <h1 class="title">{{ data!.username }}{{ t("user.title") }}</h1>
    <div>
      <p>{{ userId }}</p>
    </div>
  </div>
</template>

<style lang="css" scoped></style>
