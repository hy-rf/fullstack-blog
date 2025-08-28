package com.backend.dto.post;

import java.util.Optional;
import lombok.Data;

@Data
public class CreatePostRequest {
  private String content;
  private Optional<Long> postId = Optional.empty();
}
