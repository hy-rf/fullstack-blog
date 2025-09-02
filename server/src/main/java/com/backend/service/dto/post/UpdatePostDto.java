package com.backend.service.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePostDto {

  private Integer postId;
  private Integer authorId;
  private String content;
}
