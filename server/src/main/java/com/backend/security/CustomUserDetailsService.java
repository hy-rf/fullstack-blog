package com.backend.security;

import com.backend.common.JwtData;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  public UserDetails loadUserById(Integer id) {
    throw new UnsupportedOperationException(
      "Unimplemented method 'loadUserById'"
    );
  }

  public UserDetails loadUserFromToken(JwtData data) {
    JwtData user = new JwtData(
      data.getUserId(),
      data.getUserName(),
      data.getRoleNames()
    );
    return new CustomUserDetails(user);
  }

  @Override
  public UserDetails loadUserByUsername(String username)
    throws UsernameNotFoundException {
    throw new UnsupportedOperationException(
      "Unimplemented method 'loadUserByUsername'"
    );
  }
}
