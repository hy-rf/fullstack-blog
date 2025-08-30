package com.backend.service.migration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostData {
  private Integer authorId;
  private String content;
}
