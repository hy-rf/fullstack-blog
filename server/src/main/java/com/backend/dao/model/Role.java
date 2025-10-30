package com.backend.dao.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Getter
@Table("roles")
public class Role {

  @Id
  private Integer id;

  private String name;
}
