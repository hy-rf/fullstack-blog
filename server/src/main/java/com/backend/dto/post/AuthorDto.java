package com.backend.dto.post;

import java.util.List;

import com.backend.model.Role;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {
  private Long id;
  private String username;
  private String email;
  private List<Role> roles;
}
