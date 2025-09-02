package com.backend.controller.dto.post;

import java.util.Optional;
import lombok.Data;

@Data
public class CreatePostRequest {

  private String content;
  private Optional<Integer> rootPostId = Optional.empty();
  private Optional<Integer> postId = Optional.empty();
}
