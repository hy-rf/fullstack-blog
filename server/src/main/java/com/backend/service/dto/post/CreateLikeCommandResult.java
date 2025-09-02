package com.backend.service.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreateLikeCommandResult {

  private Integer postId;
  private Integer userId;
}
