package com.backend.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

import com.backend.model.User;

@Repository
public interface UserRepository extends BaseRepository<User, Long> {

  @Override
  @NonNull
  Optional<User> findById(@NonNull Long id);

  // @EntityGraph(attributePaths = "roles")
  Optional<User> findByUsername(String username);

  @Override
  @EntityGraph(attributePaths = { "roles" }) // Solves n+1 query
  @NonNull
  Page<User> findAll(@NonNull Pageable pageable);

}