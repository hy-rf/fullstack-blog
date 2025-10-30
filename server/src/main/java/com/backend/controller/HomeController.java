package com.backend.controller;

import com.backend.dao.PostRepository;
import com.backend.dao.UserRepository;
import com.backend.dao.model.Post;
import com.backend.dao.model.User;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class HomeController {

  private final UserRepository userRepository;

  private final PostRepository postRepository;

  @GetMapping("/")
  @PreAuthorize("permitAll()")
  public void index(HttpServletResponse response) {
    try {
      response.sendRedirect("/swagger-ui.html");
    } catch (Exception e) {
      log.error("Redirect failed", e);
    }
  }

  @GetMapping("test-u")
  public ResponseEntity<User> u() {
    User user = userRepository.findById(1).get();
    return ResponseEntity.ok(user);
  }

  @GetMapping("test-p")
  public ResponseEntity<Post> p() {
    Post user = postRepository.findById(1).get();
    return ResponseEntity.ok(user);
  }
}
