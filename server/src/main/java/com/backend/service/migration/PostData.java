package com.backend.service.migration;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostData {
  private Long authorId;
  private String title;
  private String content;
}
