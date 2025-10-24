package com.backend.dao;

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

  private OffsetDateTime createdAt;

  private Integer authorId;

  private Integer rootPostId;

  private Integer parentPostId;

  private Integer likeCount;

  private Integer saveCount;

  private Integer postCount;
}
