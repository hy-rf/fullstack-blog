package com.backend.viewmodel.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class LoginRequest {
    @Schema(description = "Name of the user", example = "admin")
    private String username;
    @Schema(description = "Password of the user", example = "gykn4f5-7g2az2r")
    private String password;
}
