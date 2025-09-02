package com.backend.service.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RemoveLikeCommandResult {

  private Integer postId;
  private Integer userId;
}
