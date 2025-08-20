package com.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.backend.model.Post;

import jakarta.annotation.Nullable;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

  Optional<List<Post>> findByAuthorId(Long userId);
  
  @Override
  @EntityGraph(attributePaths = { "author" })
  Page<Post> findAll(Specification<Post> spec, Pageable pageable);

}
