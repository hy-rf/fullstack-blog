package com.backend.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.backend.repository.UserRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import com.backend.common.PasswordUtils;
import com.backend.dto.user.UpdateUserFieldResult;
import com.backend.dto.user.UpdateUserRequest;
import com.backend.dto.user.UpdateUserResult;
import com.backend.model.User;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;
    private final PasswordUtils passwordUtils;

    public UserService(UserRepository userRepository, PasswordUtils passwordUtils) {
        this.userRepository = userRepository;
        this.passwordUtils = passwordUtils;
    }

    // Implement methods for user management, such as creating, updating, and
    // deleting users.
    // This class will interact with the UserRepository to perform database
    // operations.

    // Example method:
    public void createUser(String username, String password) {
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

    public User getUserById(Long id) {
        return userRepository.findById(id).get();
    }

    @Transactional
    public UpdateUserResult updateUser(UpdateUserRequest updateUserRequest) {
        Optional<User> userOpt = userRepository.findById(updateUserRequest.getId());
        if (!userOpt.isPresent()) {
            log.error("User with ID {} not found", updateUserRequest.getId());
            throw new RuntimeException("User not found");
        }
        User user = userOpt.get();
        UpdateUserResult updateUserResult = new UpdateUserResult(user.getId(), null, null, null);

        if (updateUserRequest.getUsername() != null) {
            updateUserResult.setUsername(new UpdateUserFieldResult<>("username", user.getUsername(),
                    updateUserRequest.getUsername()));
            user.setUsername(updateUserRequest.getUsername());
        }

        if (updateUserRequest.getEmail() != null) {
            updateUserResult.setEmail(new UpdateUserFieldResult<>("email", user.getEmail(),
                    updateUserRequest.getEmail()));
            user.setEmail(updateUserRequest.getEmail());
        }

        if (updateUserRequest.getPassword() != null) {
            updateUserResult
                    .setPassword(new UpdateUserFieldResult<>("password", "********", "********"));
            String hashedPassword = passwordUtils.hashPassword(updateUserRequest.getPassword());
            user.setPasswordHash(hashedPassword);
        }
        userRepository.save(user);
        return updateUserResult;
    }

}
