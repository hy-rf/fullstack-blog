package com.backend.service.migration;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UserData {
  private String username;
  private String password;
}
