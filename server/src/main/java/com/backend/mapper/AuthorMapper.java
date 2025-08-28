package com.backend.mapper;

import com.backend.dto.post.AuthorDto;
import com.backend.model.User;

public class AuthorMapper {
  public AuthorDto toAuthorDto(User user) {
    AuthorDto authorDto = new AuthorDto();
    authorDto.setId(user.getId());
    authorDto.setUsername(user.getUsername());
    authorDto.setRoles(user.getRoles());
    return authorDto;
  }
}
