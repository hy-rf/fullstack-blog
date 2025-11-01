package com.backend.service.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePostResultDto {

  private UpdatePostResultStatus updatePostResultStatus;
  private String message;
}
