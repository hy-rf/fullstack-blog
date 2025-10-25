package com.backend.security;

import com.backend.common.JwtData;
import java.util.Collection;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

public class CustomUserDetails implements UserDetails {

  private final Integer id;
  private final String username;
  private final String password;
  private final Collection<? extends GrantedAuthority> authorities;

  public CustomUserDetails(JwtData user) {
    this.id = user.getUserId();
    this.username = user.getUserName();
    this.password = "";
    this.authorities = user
      .getRoleNames()
      .stream()
      .map(roleName -> (GrantedAuthority) () -> "ROLE_" + roleName)
      .collect(Collectors.toList());
  }

  public Integer getId() {
    return id;
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return authorities;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }
}
