package com.backend.controller.dto.post;

import lombok.Data;

@Data
public class UpdatePostRequest {

  private Integer postId;
  private String content;
}
