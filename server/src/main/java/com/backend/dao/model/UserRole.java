package com.backend.dao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Getter
@Table("user_roles")
public class UserRole {

  private Integer roleId;
}
