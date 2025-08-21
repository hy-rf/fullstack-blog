package com.backend.model;

import org.springframework.data.redis.core.RedisHash;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
@RedisHash("login_attempts")
public class LoginAttempt {
    @Id
    private String id;
    // add fields as needed

    private int times;
    // getters and setters
}