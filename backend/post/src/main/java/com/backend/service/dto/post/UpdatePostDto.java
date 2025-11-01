package com.backend.service.dto.post;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.lang.NonNull;

@Data
@AllArgsConstructor
public class UpdatePostDto {

  @NonNull
  private Integer postId;

  private Integer authorId;
  private String content;
}
