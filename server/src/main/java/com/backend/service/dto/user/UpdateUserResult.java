package com.backend.service.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateUserResult {

  private Long id;

  private UpdateUserFieldResult<String> username;

  private UpdateUserFieldResult<String> email;

  private UpdateUserFieldResult<String> password;
}
