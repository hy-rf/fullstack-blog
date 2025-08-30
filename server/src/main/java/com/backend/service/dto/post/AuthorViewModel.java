package com.backend.service.dto.post;

import java.util.List;

import com.backend.model.Role;

import lombok.Data;

@Data
public class AuthorViewModel {
  private Integer id;
  private String username;
  private List<Role> roles;
}
