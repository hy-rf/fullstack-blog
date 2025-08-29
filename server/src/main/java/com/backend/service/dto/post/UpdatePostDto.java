package com.backend.service.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePostDto {
  private Long postId;
  private Long authorId;
  private String content;
}
