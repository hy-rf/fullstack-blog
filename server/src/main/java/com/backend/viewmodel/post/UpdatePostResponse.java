package com.backend.viewmodel.post;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UpdatePostResponse {
  private Boolean success;
  private String message;
}
