package com.backend.controller.dto.user;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBasicDto {

  private Integer id;
  private String username;
  private OffsetDateTime createdAt;
  private List<String> roles = new ArrayList<>();
  private List<String> avatarUrls = new ArrayList<>();
}
