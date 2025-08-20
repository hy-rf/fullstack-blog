import { WebSocketServer, WebSocket } from 'ws';
import type { IncomingMessage } from 'http';
import type { Duplex } from 'stream';
import { defineEventHandler } from '#imports';

export default defineEventHandler(async (event) => {
  const wss = new WebSocketServer({ noServer: true });

  const { req, res } = event.node;
  const { socket } = res;

  if (socket && req.headers.upgrade === 'websocket') {
    wss.handleUpgrade(req as IncomingMessage, socket as Duplex, Buffer.alloc(0), (ws) => {
      console.log('Client connected');

      const backendWs = new WebSocket('ws://localhost:8080/ws/chat');

      backendWs.on('open', () => {
        console.log('Connected to backend WebSocket');
      });

      ws.on('message', (message) => {
        console.log('received from client: %s', message);
        if (backendWs.readyState === WebSocket.OPEN) {
          backendWs.send(message);
        }
      });

      backendWs.on('message', (message) => {
        console.log('received from backend: %s', message);
        if (ws.readyState === WebSocket.OPEN) {
          ws.send(message);
        }
      });

      ws.on('close', () => {
        console.log('Client disconnected');
        if (backendWs.readyState === WebSocket.OPEN) {
          backendWs.close();
        }
      });

      backendWs.on('close', () => {
        console.log('Backend WebSocket disconnected');
        if (ws.readyState === WebSocket.OPEN) {
          ws.close();
        }
      });

      ws.on('error', (error) => {
        console.error('Client WebSocket error:', error);
      });

      backendWs.on('error', (error) => {
        console.error('Backend WebSocket error:', error);
      });
    });
  } else {
    res.statusCode = 400;
    res.end('WebSocket upgrade required');
  }
});
