package com.backend.service;

import com.backend.model.Role;
import com.backend.repository.RoleRepository;
import java.util.List;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

  private final RoleRepository roleRepository;

  public RoleService(RoleRepository roleRepository) {
    this.roleRepository = roleRepository;
  }

  public List<Role> getRoles() {
    return roleRepository.findAll();
  }
}
