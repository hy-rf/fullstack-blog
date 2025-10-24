package com.backend.service;

import com.backend.common.PasswordUtils;
import com.backend.dao.User;
import com.backend.dao.UserRepository;
import com.backend.service.dto.user.UpdateUserCommand;
import com.backend.service.dto.user.UpdateUserFieldResult;
import com.backend.service.dto.user.UpdateUserResult;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserService {

  private final UserRepository userRepository;
  private final PasswordUtils passwordUtils;

  public void createUser(String username, String password, List<String> roles) {
    // Logic to create a new user
  }

  public User getUserById(Integer id) {
    return userRepository.findById(id).get();
  }

  @Transactional
  public UpdateUserResult updateUser(UpdateUserCommand updateUserRequest) {
    Optional<User> userOpt = userRepository.findById(updateUserRequest.getId());
    if (!userOpt.isPresent()) {
      log.error("User with ID {} not found", updateUserRequest.getId());
      throw new RuntimeException("User not found");
    }
    User user = userOpt.get();
    UpdateUserResult updateUserResult = new UpdateUserResult(
      user.getId(),
      null,
      null,
      null
    );

    if (updateUserRequest.getUsername() != null) {
      updateUserResult.setUsername(
        new UpdateUserFieldResult<>(
          "username",
          user.getUsername(),
          updateUserRequest.getUsername()
        )
      );
      user.setUsername(updateUserRequest.getUsername());
    }

    if (updateUserRequest.getEmail() != null) {
      updateUserResult.setEmail(
        new UpdateUserFieldResult<>(
          "email",
          user.getEmail(),
          updateUserRequest.getEmail()
        )
      );
      user.setEmail(updateUserRequest.getEmail());
    }

    if (updateUserRequest.getPassword() != null) {
      updateUserResult.setPassword(
        new UpdateUserFieldResult<>("password", "********", "********")
      );
      String hashedPassword = passwordUtils.hashPassword(
        updateUserRequest.getPassword()
      );
      user.setPasswordHash(hashedPassword);
    }
    userRepository.save(user);
    return updateUserResult;
  }
}
