package com.backend.dao.model;

import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "posts")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Post {

  @Id
  private Integer id;

  private String content;

  private OffsetDateTime createdAt = OffsetDateTime.now();

  private Integer authorId;

  private Integer rootPostId;

  private Integer parentPostId;

  private Integer likeCount = 0;

  private Integer saveCount = 0;

  private Integer postCount = 0;
}
