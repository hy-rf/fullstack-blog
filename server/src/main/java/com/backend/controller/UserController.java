package com.backend.controller;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.backend.model.User;
import com.backend.security.CustomUserDetails;
import com.backend.service.UserService;
import com.backend.viewmodel.auth.LoginRequest;
import com.backend.viewmodel.user.CreateUserRequest;
import com.backend.viewmodel.user.CreateUserResult;
import com.backend.viewmodel.user.CreateUserStatus;
import com.backend.viewmodel.user.UpdateUserRequest;
import com.backend.viewmodel.user.UpdateUserResult;

import jakarta.validation.Valid;

@RestController
public class UserController {
    @Autowired
    private UserService userService;

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/user")
    public ResponseEntity<CreateUserResult> createUser(@RequestBody @Valid CreateUserRequest createUserRequest) {
        // TODO: Call User Service
        return new ResponseEntity<CreateUserResult>(new CreateUserResult(CreateUserStatus.SUCCESS, null),
                HttpStatusCode.valueOf(200));
    }

    @PreAuthorize("hasRole('admin')")
    @PostMapping("/test-user")
    public String saveExampleUser(@RequestBody LoginRequest loginRequest) {
        System.out.println(loginRequest);
        userService.saveExampleUser(loginRequest.getUsername(), loginRequest.getPassword());
        return "User information retrieved successfully";
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/users")
    public ResponseEntity<Page<User>> getUsers(@RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        page = Math.max(1, page);
        size = Math.max(1, Math.min(size, 30));
        Page<User> user = userService.getUsers(page, size);
        return ResponseEntity.ok().body(user);
    }

    @PreAuthorize("hasRole('admin')")
    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable Integer id) {
        // Logic to retrieve user by ID

        return userService.getUserById(id.longValue());
    }

    /**
     * Updates a user based on the provided UpdateUserRequest.
     * Only accessible by users with the same id.
     *
     * @param updateUserRequest
     * @return
     */
    @PutMapping("/user")
    public ResponseEntity<UpdateUserResult> updateUser(@Valid @RequestBody UpdateUserRequest updateUserRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Long userId = userDetails.getId();
        if (!userId.equals(updateUserRequest.getId())) {
            return new ResponseEntity<>(null, HttpStatusCode.valueOf(400));
        }
        UpdateUserResult result = userService.updateUser(updateUserRequest);
        return new ResponseEntity<>(result, HttpStatusCode.valueOf(200));
    }
}
