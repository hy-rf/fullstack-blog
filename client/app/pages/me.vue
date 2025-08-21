<script setup lang="ts">
async function refresh() {
  await fetch("/api/refresh", { method: "get", credentials: "include" });
}
</script>

<template>
  <div>
    <button @click="refresh">Refresh</button>
  </div>
</template>

<style lang="css" scoped></style>
