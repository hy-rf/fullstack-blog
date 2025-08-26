package com.backend.dto.user;

import lombok.Data;

@Data
public class UpdateUserRequest {

  private Long id;

  private String username;

  private String email;

  private String password;

  private String fullName;

}
