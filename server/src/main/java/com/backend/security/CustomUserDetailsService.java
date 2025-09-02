package com.backend.security;

import com.backend.common.JwtData;
import com.backend.model.Role;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailsService implements UserDetailsService {

  private final UserRepository userRepository;

  public CustomUserDetailsService(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  public UserDetails loadUserById(Integer id) {
    User user = userRepository
      .findById(id)
      .orElseThrow(() ->
        new UsernameNotFoundException("User not found: " + id)
      );
    return new CustomUserDetails(user);
  }

  public UserDetails loadUserFromToken(JwtData data) {
    User user = new User();
    user.setId(data.getUserId());
    user.setRoles(
      data
        .getRoleNames()
        .stream()
        .map(e -> {
          Role role = new Role();
          role.setName(e);
          return role;
        })
        .toList()
    );
    user.setUsername(data.getUserName());
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
