package com.backend.viewmodel.auth;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CurrentUserResponse {
    private Long userId;
    private String username;
    private List<String> roles;
}