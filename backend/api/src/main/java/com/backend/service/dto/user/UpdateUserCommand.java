package com.backend.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
public class UpdateUserCommand {

  @NonNull
  private Integer id;

  private String username;

  private String email;

  private String password;
}
