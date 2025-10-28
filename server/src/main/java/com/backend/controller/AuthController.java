package com.backend.controller;

import com.backend.common.AuthTokenData;
import com.backend.common.CookieHelper;
import com.backend.common.JwtUtils;
import com.backend.controller.dto.auth.CurrentUserResponse;
import com.backend.controller.dto.auth.LoginRequest;
import com.backend.controller.dto.auth.RegisterRequest;
import com.backend.service.AuthService;
import com.backend.service.dto.auth.LoginResult;
import com.backend.service.dto.auth.RefreshResult;
import com.backend.service.dto.auth.RefreshStatus;
import com.backend.service.dto.auth.RegisterResult;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class AuthController {

  @Value("${auth.token.age.days}")
  private int tokenAgeDays;

  @Value("${auth.refresh.age.days}")
  private int refreshAgeDays;

  private final CookieHelper cookieHelper;
  private final JwtUtils jwtUtils;
  private final AuthService authService;

  @PostMapping("/register")
  @Operation(summary = "Register")
  public ResponseEntity<String> signup(
    @Valid @RequestBody RegisterRequest registerRequest,
    HttpSession session,
    HttpServletResponse response
  ) {
    RegisterResult result = authService.registerUser(
      registerRequest.username,
      registerRequest.password
    );
    return switch (result.getStatus()) {
      case SUCCESS -> ResponseEntity.ok().body("User registered successfully");
      case USERNAME_TAKEN -> ResponseEntity.badRequest().body(
        "Username is already taken"
      );
      case INVALID_PASSWORD -> ResponseEntity.badRequest().body(
        "Invalid password format"
      );
      case ERROR -> ResponseEntity.badRequest().body(
        "An error occurred during registration"
      );
      default -> ResponseEntity.badRequest().body(
        "Unknown registration status"
      );
    };
  }

  @PostMapping("/login")
  @Operation(summary = "Login as user")
  public ResponseEntity<String> login(
    @RequestBody LoginRequest loginRequest,
    HttpSession session,
    HttpServletResponse response
  ) {
    LoginResult result = authService.loginUser(
      loginRequest.getUsername(),
      loginRequest.getPassword()
    );
    return switch (result.getStatus()) {
      case SUCCESS -> {
        String token = result.getToken();
        Cookie tokenCookie = cookieHelper.createCookie(
          "token",
          token,
          tokenAgeDays
        );
        response.addCookie(tokenCookie);
        String refreshToken = result.getRefresh();
        Cookie refreshTokenCookie = cookieHelper.createCookie(
          "refresh",
          refreshToken,
          refreshAgeDays
        );
        response.addCookie(refreshTokenCookie);
        yield ResponseEntity.ok("Login successful");
      }
      case USER_NOT_FOUND -> ResponseEntity.badRequest().body("");
      case INVALID_PASSWORD -> ResponseEntity.badRequest().body("");
      case ERROR -> ResponseEntity.badRequest().body("");
      default -> ResponseEntity.badRequest().body("");
    };
  }

  @GetMapping("/refresh")
  public String refresh(
    HttpServletRequest request,
    HttpServletResponse response
  ) {
    String token = jwtUtils.resolveToken(request);
    String refreshToken = jwtUtils.resolveRefreshToken(request);
    RefreshResult result = authService.refreshToken(token, refreshToken);
    if (result.getRefreshStatus() == RefreshStatus.FAIL) {
      return "Fail";
    }
    Cookie tokenCookie = cookieHelper.createCookie(
      "token",
      result.getNewToken(),
      tokenAgeDays
    );
    response.addCookie(tokenCookie);
    Cookie refreshTokenCookie = cookieHelper.createCookie(
      "refresh",
      result.getNewFreshToken(),
      refreshAgeDays
    );
    response.addCookie(refreshTokenCookie);
    return "Token refreshed successfully";
  }

  @GetMapping("/leave")
  @PreAuthorize("isAuthenticated()")
  public String logout(HttpServletResponse response) {
    Cookie tokenCookie = cookieHelper.createCookie("token", null, 0);
    response.addCookie(tokenCookie);
    Cookie refreshCookie = cookieHelper.createCookie("refresh", null, 0);
    response.addCookie(refreshCookie);
    return "User logged out successfully";
  }

  @PreAuthorize("isAuthenticated()")
  @GetMapping("/me")
  public ResponseEntity<CurrentUserResponse> getCurrentUser() {
    Authentication authentication =
      SecurityContextHolder.getContext().getAuthentication();
    AuthTokenData userDetails = (AuthTokenData) authentication.getPrincipal();
    Integer userId = userDetails.getId().intValue();
    String username = authentication.getName();
    List<String> roles = authentication
      .getAuthorities()
      .stream()
      .map(auth -> auth.getAuthority())
      .toList();
    return ResponseEntity.ok(new CurrentUserResponse(userId, username, roles));
  }

  @PostMapping("/check")
  public ResponseEntity<Boolean> isUsernameAvailable() {
    return ResponseEntity.ok(true);
  }
}
