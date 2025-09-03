package com.backend.repository.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class SavedPost {

  private Integer postId;
  private Integer userId;
}