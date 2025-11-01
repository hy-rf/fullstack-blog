package com.backend.controller.dto;

import lombok.Data;

@Data
public class UpdateUserRequest {

  private Integer id;

  private String username;

  private String email;

  private String password;
}
