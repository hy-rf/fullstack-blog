package com.backend.service;

import com.backend.common.PasswordUtils;
import com.backend.dao.UserRepository;
import com.backend.dao.model.User;
import com.backend.service.dto.user.UpdateUserCommand;
import com.backend.service.dto.user.UpdateUserResult;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordUtils passwordUtils;

  public void createUser(String username, String password, List<String> roles) {
    // Logic to create a new user
  }

  public User getUserById(@NonNull Integer id) {
    return userRepository.findById(id).get();
  }

  @Transactional
  public UpdateUserResult updateUser(UpdateUserCommand updateUserRequest) {
    User user = userRepository
      .findById(updateUserRequest.getId())
      .orElseThrow(() -> {
        log.error("User with ID {} not found", updateUserRequest.getId());
        return new RuntimeException("User not found");
      });

    if (updateUserRequest.getUsername() != null) {
      user.setUsername(updateUserRequest.getUsername());
    }

    if (updateUserRequest.getPassword() != null) {
      user.setPasswordHash(
        passwordUtils.hashPassword(updateUserRequest.getPassword())
      );
    }
    if (user == null) {
      throw new IllegalStateException("User should not be null");
    }
    userRepository.save(user);

    return new UpdateUserResult(user.getId(), user);
  }
}
