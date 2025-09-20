package com.backend.repository;

import com.backend.model.PostHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostHistoryRepository
  extends JpaRepository<PostHistory, Integer> {}
