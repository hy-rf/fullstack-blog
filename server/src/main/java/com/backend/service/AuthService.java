package com.backend.service;

import com.backend.common.JwtData;
import com.backend.common.JwtUtils;
import com.backend.common.PasswordUtils;
import com.backend.model.Role;
import com.backend.model.User;
import com.backend.repository.RoleRepository;
import com.backend.repository.UserRepository;
import com.backend.service.dto.auth.LoginResult;
import com.backend.service.dto.auth.LoginStatus;
import com.backend.service.dto.auth.RefreshResult;
import com.backend.service.dto.auth.RefreshStatus;
import com.backend.service.dto.auth.RegisterResult;
import com.backend.service.dto.auth.RegisterStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthService {

  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final JwtUtils jwtUtils;
  private final PasswordUtils passwordUtils;

  @Value("${auth.token.age.days}")
  private int TOKEN_AGE_IN_DAYS;

  @Value("${auth.refresh.age.days}")
  private int REFRESH_TOKEN_AGE_IN_DAYS;

  @Value("${jwt.secret}")
  private String JWT_SECRET;

  @Value("${jwt.secret.refresh}")
  private String REFRESH_JWT_SECRET;

  public AuthService(
    UserRepository userRepository,
    RoleRepository roleRepository,
    JwtUtils jwtUtils,
    PasswordUtils passwordUtils
  ) {
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.jwtUtils = jwtUtils;
    this.passwordUtils = passwordUtils;
  }

  @Transactional
  public RegisterResult registerUser(String username, String password) {
    try {
      String hash = passwordUtils.hashPassword(password);

      User user = new User();
      user.setUsername(username);
      user.setPasswordHash(hash);

      Optional<Role> userRoleOpt = roleRepository.findByName("user");
      if (userRoleOpt.isEmpty()) {
        Role newUserRole = new Role();
        newUserRole.setName("user");
        roleRepository.save(newUserRole);
      }
      Role userRole = userRoleOpt.get();
      List<Role> roles = new ArrayList<>();
      roles.add(userRole);
      user.setRoles(roles);
      User newUser = userRepository.save(user);
      if (newUser.getId().equals(1)) {
        Optional<Role> adminRoleOpt = roleRepository.findByName("admin");
        if (adminRoleOpt.isEmpty()) {
          Role newAdminRole = new Role();
          newAdminRole.setName("admin");
          roleRepository.save(newAdminRole);
        }
        Role adminRole = adminRoleOpt.get();
        newUser.getRoles().add(adminRole);
        userRepository.save(newUser);
      }
      return new RegisterResult(RegisterStatus.SUCCESS);
    } catch (Exception e) {
      throw new RuntimeException("Registration failed: " + e.getMessage(), e);
    }
  }

  public LoginResult loginUser(String username, String password) {
    Optional<User> userOpt = userRepository.findByUsername(username);
    if (userOpt.isEmpty()) {
      return new LoginResult(
        "User not found",
        LoginStatus.USER_NOT_FOUND,
        null,
        null
      );
    }
    User user = userOpt.get();
    if (!passwordUtils.verifyPassword(password, user.getPasswordHash())) {
      return new LoginResult(
        "Invalid password",
        LoginStatus.INVALID_PASSWORD,
        null,
        null
      );
    }

    Integer userId = user.getId();
    List<Role> roles = user.getRoles();
    Stream<Role> roleStream = roles.stream();
    Stream<String> roleNameStream = roleStream.map(Role::getName);
    List<String> roleNames = roleNameStream.toList();
    String token = jwtUtils.generateToken(
      userId,
      roleNames,
      JWT_SECRET,
      TOKEN_AGE_IN_DAYS,
      username
    );
    String refreshToken = jwtUtils.generateToken(
      userId,
      roleNames,
      REFRESH_JWT_SECRET,
      REFRESH_TOKEN_AGE_IN_DAYS,
      username
    );
    return new LoginResult(
      "Login successful",
      LoginStatus.SUCCESS,
      token,
      refreshToken
    );
  }

  public void logoutUser() {
    // Logic to log out a user
  }

  public RefreshResult refreshToken(String token, String refreshToken) {
    JwtData refreshData = jwtUtils.verifyToken(
      refreshToken,
      REFRESH_JWT_SECRET
    );
    String newToken = jwtUtils.generateToken(
      refreshData.getUserId(),
      refreshData.getRoleNames(),
      JWT_SECRET,
      TOKEN_AGE_IN_DAYS,
      refreshData.getUserName()
    );
    return new RefreshResult(RefreshStatus.SUCCESS, newToken, refreshToken);
  }
}
