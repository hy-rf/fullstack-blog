package com.backend.service.dto.post;

import com.backend.model.Role;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthorDto {

  private Integer id;
  private String username;
  private List<Role> roles;
}
