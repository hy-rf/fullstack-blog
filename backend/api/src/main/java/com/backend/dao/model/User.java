package com.backend.dao.model;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.MappedCollection;
import org.springframework.data.relational.core.mapping.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("users")
public class User {

  @Id
  private Integer id;

  private String username;

  private String passwordHash;

  private OffsetDateTime createdAt = OffsetDateTime.now();

  @MappedCollection(idColumn = "user_id")
  private Set<UserRole> userRoles = new HashSet<>();
}
