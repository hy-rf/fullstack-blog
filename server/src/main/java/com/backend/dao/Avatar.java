package com.backend.dao;

import java.time.OffsetDateTime;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "avatars")
@Data
public class Avatar {

  private Integer userId;

  @Id
  private String url;

  private OffsetDateTime createdAt;
}
