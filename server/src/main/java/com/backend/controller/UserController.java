package com.backend.controller;

import com.backend.controller.dto.user.CreateUserRequest;
import com.backend.controller.dto.user.UpdateAvatarRequest;
import com.backend.controller.dto.user.UpdateUserRequest;
import com.backend.controller.dto.user.UserBasicDto;
import com.backend.mapper.UserMapper;
import com.backend.model.User;
import com.backend.repository.UserRepository;
import com.backend.security.CustomUserDetails;
import com.backend.service.UploadService;
import com.backend.service.UserService;
import com.backend.service.dto.user.CreateUserResult;
import com.backend.service.dto.user.CreateUserStatus;
import com.backend.service.dto.user.UpdateUserCommand;
import com.backend.service.dto.user.UpdateUserResult;
import jakarta.validation.Valid;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class UserController {

  private final long MAX_FILE_SIZE = 5 * 1024 * 1024;

  private final UserService userService;
  private final UploadService uploadService;
  private final UserRepository userRepository;

  private final UserMapper userMapper;

  @PostMapping("/user/avatar")
  @PreAuthorize("hasRole('user')")
  public ResponseEntity<String> uploadAvatar(
    @RequestBody UpdateAvatarRequest updateAvatarRequest
  ) throws IOException {
    Integer userId =
      ((CustomUserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getId();
    String f = updateAvatarRequest.getImage();
    String[] fileStrings = f.split(",");
    byte[] file = Base64.getDecoder().decode(
      fileStrings[1].getBytes(StandardCharsets.UTF_8)
    );
    if (file.length > MAX_FILE_SIZE) {
      return ResponseEntity.badRequest().body("Too big");
    }
    uploadService.save(
      file,
      "avatar",
      Integer.valueOf(userId),
      fileStrings[0].split(";")[0].split("/")[1]
    );

    return ResponseEntity.ok("File uploaded successfully.");
  }

  @PostMapping("/user/avatar-base64")
  @PreAuthorize("hasRole('user')")
  public ResponseEntity<String> uploadAvatar(String base64String) {
    // TODO: turn base64 string to file then upload avatar by calling UploadService
    return ResponseEntity.ok("Success");
  }

  /**
   *
   * @param createUserRequest
   * @return
   */
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

  /**
   * List Users
   * @param page
   * @param size
   * @return
   */
  @PreAuthorize("hasRole('admin')")
  @GetMapping("/users")
  public ResponseEntity<List<UserBasicDto>> getUsers(
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "100") int size
  ) {
    List<UserBasicDto> users = userMapper.selectAll((page - 1) * size, size);
    return ResponseEntity.ok().body(users);
  }

  /**
   * for user main page
   * @param id
   * @return
   */
  @GetMapping("/user/{id}")
  public ResponseEntity<UserBasicDto> getUserById(@PathVariable Integer id) {
    UserBasicDto user = userMapper.selectBasicById(id);
    return ResponseEntity.ok(user);
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
  @PreAuthorize("hasRole('admin')")
  public ResponseEntity<UpdateUserResult> updateUser(
    @Valid @RequestBody UpdateUserRequest updateUserRequest
  ) {
    Integer userId =
      ((CustomUserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getId();
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

  @GetMapping("/following")
  @PreAuthorize("hasRole('user')")
  public ResponseEntity<List<User>> getFollowing() {
    Integer userId =
      ((CustomUserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getId();
    List<User> users = userRepository.findById(userId).get().getFollowings();
    return ResponseEntity.ok(users);
  }
}
