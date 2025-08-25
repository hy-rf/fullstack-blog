package com.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;

import com.backend.model.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

  Optional<List<Post>> findByAuthorId(Long userId);

  @Override
  @NonNull
  @EntityGraph(attributePaths = { "author", "replies", "replies" }) // Eagerly fetch replies
  Optional<Post> findById(@NonNull Long id);

  @Override
  @NonNull
  @EntityGraph(attributePaths = { "author", "replies" })
  Page<Post> findAll(@Nullable Specification<Post> spec, @NonNull Pageable pageable);

}
