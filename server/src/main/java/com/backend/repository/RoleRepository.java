package com.backend.repository;

import com.backend.model.Role;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends BaseRepository<Role, Integer> {
  Optional<Role> findByName(String string);
}
