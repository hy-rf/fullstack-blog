package com.backend.controller;

import java.util.Optional;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class CreateReplyRequest {
  private Optional<Long> postId = Optional.empty();
  private Optional<Long> parentReplyId = Optional.empty();

  @NotBlank
  private String content;
}
