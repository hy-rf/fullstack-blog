package com.backend.service.dto.post;

import java.util.List;

import com.backend.model.Role;

import lombok.Data;

@Data
public class AuthorViewModel {
  private Long id;
  private String username;
  private String email;
  private List<Role> roles;
}
