package com.backend.repository;

import com.backend.model.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends BaseRepository<User, Integer> {
  @Override
  @NonNull
  Optional<User> findById(@NonNull Integer id);

  // @EntityGraph(attributePaths = "roles")
  Optional<User> findByUsername(String username);

  @Override
  @EntityGraph(attributePaths = { "roles" }) // Solves n+1 query
  @NonNull
  Page<User> findAll(@NonNull Pageable pageable);
}
