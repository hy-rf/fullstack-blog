package com.backend.service.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class RemoveLikeCommand {

  private Integer postId;
  private Integer userId;
}
