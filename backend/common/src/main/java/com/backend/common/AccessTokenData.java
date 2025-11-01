package com.backend.common;

import java.util.Collection;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenData implements UserDetails, UserDetailsService {

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

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'loadUserByUsername'"
    );
  }
}
