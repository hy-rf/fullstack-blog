package com.backend.controller.dto.post;

import java.math.BigInteger;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PostSummary {
  private Integer id;
  private String content;
  private Instant createdAt;
  private String authorName;
  private Integer postCount;
}
