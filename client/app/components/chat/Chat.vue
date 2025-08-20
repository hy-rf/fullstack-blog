<script setup lang="ts">
import { ref, onMounted, onBeforeUnmount } from "vue";

const wsUrl = "wss://localhost:3000/api/chat";

let ws: WebSocket | null = null;
const isConnecting = ref(false);
const isOpen = ref(false);

function connect() {
  if (import.meta.server) return;

  ws = new WebSocket(wsUrl);

  ws.addEventListener("open", () => {
    console.log("opened");
  });
  ws.addEventListener("message", () => {});
  ws.addEventListener("error", () => {});
  ws.addEventListener("close", () => {
    console.log("closed");
  });
}
</script>

<template>
  <div class="chat-container">
    <div class="status">
      <span v-if="isOpen">🟢 Connected</span>
      <span v-else-if="isConnecting">🟡 Connecting…</span>
      <span v-else>🔴 Disconnected</span>
      <button v-if="!isOpen && !isConnecting" @click="connect">Connect</button>
    </div>
  </div>
</template>

<style scoped>
.chat-container {
  border: 1px solid #ccc;
  border-radius: 8px;
  padding: 12px;
  max-width: 360px;
  margin: 0 auto;
  font-family: system-ui, sans-serif;
}
.status {
  display: flex;
  gap: 8px;
  align-items: center;
  margin-bottom: 8px;
  font-size: 0.85rem;
}
.messages {
  max-height: 200px;
  overflow-y: auto;
  margin-bottom: 8px;
}
.message {
  padding: 2px 0;
  font-size: 0.8rem;
}
.input-container {
  display: flex;
  gap: 6px;
}
input {
  flex: 1;
  padding: 6px;
  border: 1px solid #ccc;
  border-radius: 4px;
}
button {
  padding: 6px 12px;
  background: #007bff;
  color: #fff;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}
button[disabled] {
  opacity: 0.5;
  cursor: not-allowed;
}
button:not([disabled]):hover {
  background: #0062c9;
}
</style>
