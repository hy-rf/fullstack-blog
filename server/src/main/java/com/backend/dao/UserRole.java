package com.backend.dao;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Getter
@Table("user_roles")
public class UserRole {

  @Id
  private Integer userId;

  private Integer roleId;
}
