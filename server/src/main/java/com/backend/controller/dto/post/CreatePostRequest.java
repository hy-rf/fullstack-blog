package com.backend.controller.dto.post;

import java.util.List;
import java.util.Optional;
import lombok.Data;

@Data
public class CreatePostRequest {

  private String content;
  private Optional<Integer> rootPostId = Optional.empty();
  private Optional<Integer> postId = Optional.empty();
  private List<String> imagesBase64Strings;
  private List<String> tags;
}
