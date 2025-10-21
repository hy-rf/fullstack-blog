package com.backend.service;

import com.backend.common.PasswordUtils;
import com.backend.model.User;
import com.backend.repository.JpaUserRepository;
import com.backend.service.dto.user.UpdateUserCommand;
import com.backend.service.dto.user.UpdateUserFieldResult;
import com.backend.service.dto.user.UpdateUserResult;
import jakarta.transaction.Transactional;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class UserService {

  private final JpaUserRepository userRepository;
  private final PasswordUtils passwordUtils;

  public UserService(
    JpaUserRepository userRepository,
    PasswordUtils passwordUtils
  ) {
    this.userRepository = userRepository;
    this.passwordUtils = passwordUtils;
  }

  public void createUser(String username, String password, List<String> roles) {
    // Logic to create a new user
  }

  public Page<User> getUsers(int page, int size) {
    Pageable pageable = PageRequest.of(page - 1, size);
    return userRepository.findAll(pageable);
  }

  public List<User> getAllUsers() {
    log.info("Fetching all users from the database.");
    return userRepository.findAll();
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
