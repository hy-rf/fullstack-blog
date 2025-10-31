package com.backend.common;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenData implements UserDetails {

  private Integer id;
  private String username;
  private List<String> roleNames;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return roleNames
      .stream()
      .map(roleName -> (GrantedAuthority) () -> "ROLE_" + roleName)
      .toList();
  }

  @Override
  public String getPassword() {
    return "";
  }

  @Override
  public String getUsername() {
    return username;
  }

  public Integer getId() {
    return id;
  }

  public List<String> getRoleNames() {
    return roleNames;
  }
}
