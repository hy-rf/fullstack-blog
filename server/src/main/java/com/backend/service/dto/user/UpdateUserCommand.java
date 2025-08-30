package com.backend.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdateUserCommand {

  private Integer id;

  private String username;

  private String email;

  private String password;

}
