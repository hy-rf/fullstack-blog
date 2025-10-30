package com.backend.service.dto.user;

import com.backend.dao.model.User;
import lombok.AllArgsConstructor;
import lombok.Data;

@AllArgsConstructor
@Data
public class UpdateUserResult {

  private Integer id;

  private User user;
}
