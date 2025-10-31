package com.backend.service;

import com.backend.common.PasswordUtils;
import com.backend.dao.UserRepository;
import com.backend.dao.model.User;
import com.backend.service.dto.user.UpdateUserCommand;
import com.backend.service.dto.user.UpdateUserResult;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

    if (updateUserRequest.getUsername() != null) {
      user.setUsername(updateUserRequest.getUsername());
    }

    if (updateUserRequest.getPassword() != null) {
      user.setPasswordHash(
        passwordUtils.hashPassword(updateUserRequest.getPassword())
      );
    }
    userRepository.save(user);
    return new UpdateUserResult(user.getId(), user);
  }
}
