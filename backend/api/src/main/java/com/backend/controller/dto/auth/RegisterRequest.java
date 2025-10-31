package com.backend.controller.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public class RegisterRequest {

  @NotBlank(message = "Username must not be blank")
  @Schema(description = "Username", example = "admin")
  public String username;

  @NotBlank(message = "Password must not be blank")
  @Schema(description = "Password", example = "admin")
  public String password;
}
