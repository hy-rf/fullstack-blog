package com.backend.dto.post;

import java.time.OffsetDateTime;

import lombok.Data;

@Data
public class PostListViewModel {
  private Long id;
  private String content;
  private AuthorViewModel author;
  private OffsetDateTime createdAt;
  private int postCount;
}
