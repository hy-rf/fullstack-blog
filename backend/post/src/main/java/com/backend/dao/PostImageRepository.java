package com.backend.dao;

import com.backend.dao.model.PostImage;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostImageRepository
  extends CrudRepository<PostImage, Integer> {}
