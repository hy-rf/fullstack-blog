package com.backend.viewmodel.chat;

import lombok.Data;

@Data
public class ChatMessage {
    private String sender;
    private String content;
    private String timestamp;
}