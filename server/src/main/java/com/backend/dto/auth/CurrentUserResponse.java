package com.backend.dto.auth;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CurrentUserResponse {
    private Long id;
    private String username;
    private List<String> roles;
}