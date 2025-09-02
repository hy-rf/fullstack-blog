package com.backend.controller;

import com.backend.controller.dto.user.CreateUserRequest;
import com.backend.controller.dto.user.UpdateUserRequest;
import com.backend.model.User;
import com.backend.security.CustomUserDetails;
import com.backend.service.UserService;
import com.backend.service.dto.user.CreateUserResult;
import com.backend.service.dto.user.CreateUserStatus;
import com.backend.service.dto.user.UpdateUserCommand;
import com.backend.service.dto.user.UpdateUserResult;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

  private final UserService userService;

  public UserController(UserService userService) {
    this.userService = userService;
  }

  @PreAuthorize("hasRole('admin')")
  @PostMapping("/user")
  public ResponseEntity<CreateUserResult> createUser(
    @RequestBody @Valid CreateUserRequest createUserRequest
  ) {
    // TODO: Call User Service
    return new ResponseEntity<CreateUserResult>(
      new CreateUserResult(CreateUserStatus.SUCCESS, null),
      HttpStatusCode.valueOf(200)
    );
  }

  @PreAuthorize("hasRole('admin')")
  @GetMapping("/users")
  public ResponseEntity<Page<User>> getUsers(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "100") int size
  ) {
    page = Math.max(1, page);
    size = Math.max(1, Math.min(size, 1000));
    Page<User> user = userService.getUsers(page, size);
    return ResponseEntity.ok().body(user);
  }

  @PreAuthorize("hasRole('admin')")
  @GetMapping("/user/{id}")
  public User getUserById(@PathVariable Integer id) {
    // Logic to retrieve user by ID

    return userService.getUserById(id);
  }

  /**
   * Updates a user based on the provided UpdateUserRequest. Only accessible by
   * users with the
   * same id.
   *
   * @param updateUserRequest
   * @return
   */
  @PutMapping("/user")
  public ResponseEntity<UpdateUserResult> updateUser(
    @Valid @RequestBody UpdateUserRequest updateUserRequest
  ) {
    Authentication authentication =
      SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails =
      (CustomUserDetails) authentication.getPrincipal();
    Integer userId = userDetails.getId();
    updateUserRequest.setId(userId);
    UpdateUserCommand updateCommand = new UpdateUserCommand(
      userId,
      updateUserRequest.getUsername(),
      updateUserRequest.getEmail(),
      updateUserRequest.getPassword()
    );
    UpdateUserResult result = userService.updateUser(updateCommand);
    return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
  }
}
