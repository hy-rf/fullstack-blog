package com.backend.service;

import com.backend.common.JwtData;
import com.backend.common.JwtUtils;
import com.backend.common.PasswordUtils;
import com.backend.dao.RoleRepository;
import com.backend.dao.User;
import com.backend.dao.UserRepository;
import com.backend.dao.UserRole;
import com.backend.service.dto.auth.LoginResult;
import com.backend.service.dto.auth.LoginStatus;
import com.backend.service.dto.auth.RefreshResult;
import com.backend.service.dto.auth.RefreshStatus;
import com.backend.service.dto.auth.RegisterResult;
import com.backend.service.dto.auth.RegisterStatus;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
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

  @Transactional
  public RegisterResult registerUser(String username, String password) {
    String hash = passwordUtils.hashPassword(password);

    User user = new User();
    user.setUsername(username);
    user.setPasswordHash(hash);
    user.getUserRoles().add(new UserRole(2));
    userRepository.save(user);

    return new RegisterResult(RegisterStatus.SUCCESS);
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
    Set<UserRole> roles = user.getUserRoles();
    List<String> roleNames = new ArrayList<>();
    roles.forEach(e -> {
      String name = roleRepository.findById(e.getRoleId()).get().getName();
      roleNames.add(name);
    });
    JwtData jwtUserData = new JwtData(userId, username, roleNames);
    String token = jwtUtils.generateToken(
      jwtUserData,
      JWT_SECRET,
      TOKEN_AGE_IN_DAYS
    );
    String refreshToken = jwtUtils.generateToken(
      jwtUserData,
      JWT_SECRET,
      TOKEN_AGE_IN_DAYS
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
    JwtData jwtUserData = new JwtData(
      refreshData.getUserId(),
      refreshData.getUserName(),
      refreshData.getRoleNames()
    );
    String newToken = jwtUtils.generateToken(
      jwtUserData,
      JWT_SECRET,
      TOKEN_AGE_IN_DAYS
    );
    return new RefreshResult(RefreshStatus.SUCCESS, newToken, refreshToken);
  }
}
