package com.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateUserRequest {
  private String username;
  private String password;
  private String email;
  private String firstName;
  private String lastName;
}
