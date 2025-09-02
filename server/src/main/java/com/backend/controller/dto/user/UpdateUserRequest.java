package com.backend.controller.dto.user;

import lombok.Data;

@Data
public class UpdateUserRequest {

  private Integer id;

  private String username;

  private String email;

  private String password;
}
