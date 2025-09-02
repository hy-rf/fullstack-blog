package com.backend.service.dto.post;

import com.backend.model.Role;
import java.util.List;
import lombok.Data;

@Data
public class AuthorViewModel {

  private Integer id;
  private String username;
  private List<Role> roles;
}
