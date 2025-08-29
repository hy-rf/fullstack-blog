package com.backend.controller.dto.post;

import lombok.Data;

@Data
public class UpdatePostRequest {
  private Long postId;
  private String content;
}
