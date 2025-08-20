<script setup lang="ts">
definePageMeta({
  validate: async (route) => {
    // Check if the id is made up of digits
    return typeof route.params.id === 'string' && /^\d+$/.test(route.params.id)
  }
})

const route = useRoute();
const userId = route.params.id as string;

</script>

<template>
  <div>
    <p>{{ userId }}</p>
  </div>
</template>

<style lang="css" scoped>

</style>