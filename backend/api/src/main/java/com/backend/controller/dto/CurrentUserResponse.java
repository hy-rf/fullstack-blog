package com.backend.controller.dto;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class CurrentUserResponse {

  private Integer id;
  private String username;
  private List<String> roles;
}
