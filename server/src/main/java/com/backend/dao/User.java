package com.backend.dao;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@Getter
@Table("users")
public class User {

  @Id
  private Integer id;

  private String username;

  private String email;

  private String passwordHash;

  private OffsetDateTime createdAt = OffsetDateTime.now();
}
