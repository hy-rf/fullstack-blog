package com.backend.dto.user;

import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateUserFieldResult<T> {

  private T field;
  private String oldValue;
  private String newValue;
}
