package com.backend.service;

import com.backend.model.Role;
import com.backend.repository.JpaRoleRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

  private final JpaRoleRepository roleRepository;

  public RoleService(JpaRoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public List<Role> getRoles() {
    return roleRepository.findAll();
  }
}
