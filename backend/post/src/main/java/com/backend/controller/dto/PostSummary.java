package com.backend.controller.dto;

import java.time.Instant;
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
  private Integer authorId;
  private String authorName;
  private Integer postCount;
  private Integer likeCount;
  private Integer saveCount;
  private String tags;
  private String urls;
}
