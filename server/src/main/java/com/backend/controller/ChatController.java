package com.backend.controller;

import com.backend.viewmodel.chat.ChatMessage;

import jakarta.websocket.OnOpen;
import jakarta.websocket.Session;
import jakarta.websocket.server.ServerEndpoint;
import lombok.extern.slf4j.Slf4j;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
@ServerEndpoint("/ws/chat")
public class ChatController {

    @OnOpen
    public void onOpen(Session session) {
        log.debug("WebSocket client connected: sessionId={}", session.getId());
    }

    @MessageMapping("/chat")
    @SendTo("/topic/messages")
    public ChatMessage sendMessage(ChatMessage message) {
        log.info(message.getContent());
        return message;
    }
}