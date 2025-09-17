package com.backend.service.dto.post;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class CreatePostCommandResult {

  private boolean Success;
  private Integer id;
}
