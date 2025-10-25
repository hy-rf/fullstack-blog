package com.backend.dao;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "post_images")
@Data
public class PostImage {

  @Id
  private Integer id;

  private Integer postId;

  private String url;
}
