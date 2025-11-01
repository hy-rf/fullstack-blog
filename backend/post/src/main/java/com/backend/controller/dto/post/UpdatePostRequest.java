package com.backend.controller.dto.post;

import lombok.Data;
import org.springframework.lang.NonNull;

@Data
public class UpdatePostRequest {

  @NonNull
  private Integer postId;

  private String content;
}
