package com.backend.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import java.time.OffsetDateTime;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@RequiredArgsConstructor
@Getter
@Table("users")
public class User {

  @Id
  private Integer id;

  private String username;

  @Column(unique = true, length = 100)
  private String email;

  @Column(name = "password_hash", columnDefinition = "bpchar(60)")
  private String passwordHash;

  @Column(name = "created_at")
  private OffsetDateTime createdAt = OffsetDateTime.now();
}
