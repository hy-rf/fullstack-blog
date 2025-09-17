package com.backend.repository;

import com.backend.model.PostImage;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository
  extends BaseRepository<PostImage, Integer> {}
