package com.backend.service.migration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MigrationService {

  @Autowired
  private PostDataProvider postDataProvider;

  @Autowired
  private UserDataProvider userDataProvider;

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

    UserData userData = userDataProvider.provideAdminUserData();

    User newUser = new User();
    newUser.setUsername(userData.getUsername());
    String hashedPassword = PasswordUtils.hashPassword(userData.getPassword());
    newUser.setPasswordHash(hashedPassword);

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

    List<Role> roles = new ArrayList<>();
    roles.add(userRole);
    roles.add(adminRole);
    newUser.setRoles(roles);

    userRepository.save(newUser);

    return "User with ID 1 does not exist.";
  }

  @Transactional
  public String seedUsers() {
    List<UserData> userData = userDataProvider.provide();
    userData.forEach(data -> {
      User user = new User();
      user.setUsername(data.getUsername());
      user.setPasswordHash(PasswordUtils.hashPassword(data.getPassword()));
      userRepository.save(user);
    });
    return "done";
  }

  @Transactional
  public String seedPosts() {
    List<PostData> posts = postDataProvider.provide();
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
