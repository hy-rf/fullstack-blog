package com.backend.controller.dto.post;

import java.time.OffsetDateTime;
import com.backend.service.dto.post.AuthorViewModel;
import lombok.Data;

@Data
public class PostListViewModel {
  private Long id;
  private String content;
  private AuthorViewModel author;
  private OffsetDateTime createdAt;
  private int postCount;
}
