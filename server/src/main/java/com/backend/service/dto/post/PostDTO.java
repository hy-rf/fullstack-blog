package com.backend.service.dto.post;

import java.time.OffsetDateTime;
import java.util.List;
import lombok.Data;

@Data
public class PostDTO {

  private Integer id;
  private String content;
  private AuthorDto author;
  private OffsetDateTime createdAt;
  private List<PostDTO> posts;
}
