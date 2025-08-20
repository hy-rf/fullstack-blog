package com.backend.service.migration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import com.backend.common.PasswordUtils;
import com.backend.model.Post;
import com.backend.model.Role;
import com.backend.model.User;
import com.backend.repository.PostRepository;
import com.backend.repository.RoleRepository;
import com.backend.repository.UserRepository;
import com.fasterxml.jackson.core.exc.StreamReadException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DatabindException;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MigrationService {

  @Value("${admin.username}")
  private String ADMIN_USERNAME;

  @Value("${admin.password}")
  private String ADMIN_PASSWORD;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private RoleRepository roleRepository;

  @Autowired
  private PostRepository postRepository;

  @Transactional
  public String checkAndAddDataIfUserExists() {
    Optional<User> userOpt = userRepository.findById(1L);
    if (userOpt.isPresent()) {
      return "User with ID 1 exists. No need to migrate data.";
    }
    log.warn("User with ID 1 does not exist.");

    // Create a new user
    User newUser = new User();
    newUser.setUsername(ADMIN_USERNAME);
    String hashedPassword = PasswordUtils.hashPassword(ADMIN_PASSWORD);
    newUser.setPasswordHash(hashedPassword);

    // Create roles
    Role userRole = roleRepository.findByName("user").orElseGet(() -> {
      Role role = new Role();
      role.setName("user");
      return roleRepository.save(role);
    });

    Role adminRole = roleRepository.findByName("admin").orElseGet(() -> {
      Role role = new Role();
      role.setName("admin");
      return roleRepository.save(role);
    });

    // Assign roles to the user
    List<Role> roles = new ArrayList<>();
    roles.add(userRole);
    roles.add(adminRole);
    newUser.setRoles(roles);

    // Save the user
    User user = userRepository.save(newUser);

    Post post = new Post();
    post.setTitle("title");
    post.setContent("content");
    post.setAuthor(user);

    postRepository.save(post);

    return "User with ID 1 does not exist.";
  }

  @Transactional
  public String seedUsers() {
    ObjectMapper mapper = new ObjectMapper();
    ClassPathResource resource = new ClassPathResource("users.json");
    return "done";
  }

  @Transactional
  public String seedPosts() {
    PostDataFactory factory = new PostDataFactory();
    List<PostData> posts = factory.posts();
    // Random random = new Random();
    for (PostData pd : posts) {
      long authorId = pd.getAuthorId();
      Optional<User> userOpt = userRepository.findById(authorId);
      if (userOpt.isEmpty()) {
        continue;
      }
      User user = userOpt.get();
      Post post = new Post();
      post.setTitle(pd.getTitle());
      post.setContent(pd.getContent());
      post.setAuthor(user);
      postRepository.save(post);
    }
    return "done";
  }
}
