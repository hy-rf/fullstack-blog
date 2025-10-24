package com.backend.security;

import com.backend.common.JwtData;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  public UserDetails loadUserById(Integer id) {
    // TODO Auto-generated method stub
    throw new UnsupportedOperationException(
      "Unimplemented method 'loadUserByUsername'"
    );
  }

  public UserDetails loadUserFromToken(JwtData data) {
    JwtData user = new JwtData();
    user.setUserId(data.getUserId());
    user.setRoleNames(data.getRoleNames());
    user.setUserName(data.getUserName());
    return new CustomUserDetails(user);
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
