package com.backend.repository.dto;

import java.time.Instant;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PostPage {

  private Integer id;
  private String content;
  private Instant createdAt;
  private Integer authorId;
  private String authorName;
  private Integer rootPostId;
  private Integer parentPostId;
  private Long postCount;
  private Long likeCount;
  private Long saveCount;
  private String tags;
}
