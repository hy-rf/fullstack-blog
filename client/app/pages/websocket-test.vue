<script setup lang="ts">
import { ref, onMounted, onUnmounted } from 'vue';

const message = ref('');
const receivedMessages = ref<string[]>([]);
let ws: WebSocket | null = null;

onMounted(() => {
  const wsUrl = `ws://${window.location.host}/api/websocket`;
  ws = new WebSocket(wsUrl);
  
  ws.onopen = () => {
    console.log('Connected to WebSocket');
  };
  
  ws.onmessage = (event) => {
    console.log(event.data);
    
    receivedMessages.value.push(event.data);
  };
  
  ws.onclose = () => {
    console.log('Disconnected from WebSocket');
  };
  
  ws.onerror = (error) => {
    console.error('WebSocket error:', error);
  };
});

onUnmounted(() => {
  if (ws) {
    ws.close();
  }
});

const sendMessage = () => {
  if (ws && ws.readyState === WebSocket.OPEN) {
    ws.send(message.value);
    message.value = '';
  }
};
</script>

<template>
  <div>
    <h1>WebSocket Test</h1>
    <input v-model="message" type="text" />
    <button @click="sendMessage">Send</button>
    <div>
      <h2>Received Messages:</h2>
      <ul>
        <li v-for="(msg, index) in receivedMessages" :key="index">{{ msg }}</li>
      </ul>
    </div>
  </div>
</template>