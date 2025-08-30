package com.backend.service.migration;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.backend.common.PasswordUtils;
import com.backend.model.Post;
import com.backend.model.Role;
import com.backend.model.User;
import com.backend.repository.PostRepository;
import com.backend.repository.RoleRepository;
import com.backend.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class MigrationService {

  private final PostDataProvider postDataProvider;
  private final UserDataProvider userDataProvider;
  private final UserRepository userRepository;
  private final RoleRepository roleRepository;
  private final PostRepository postRepository;
  private final PasswordUtils passwordUtils;

  public MigrationService(PostDataProvider postDataProvider, UserDataProvider userDataProvider,
      UserRepository userRepository, RoleRepository roleRepository, PostRepository postRepository,
      PasswordUtils passwordUtils) {
    this.postDataProvider = postDataProvider;
    this.userDataProvider = userDataProvider;
    this.userRepository = userRepository;
    this.roleRepository = roleRepository;
    this.postRepository = postRepository;
    this.passwordUtils = passwordUtils;
  }

  @Transactional
  public String checkAndAddDataIfUserExists() {
    Optional<User> userOpt = userRepository.findById(1);
    if (userOpt.isPresent()) {
      return "User with ID 1 exists. No need to migrate data.";
    }
    log.warn("User with ID 1 does not exist.");

    UserData userData = userDataProvider.provideAdminUserData();

    User newUser = new User();
    newUser.setUsername(userData.getUsername());
    String hashedPassword = passwordUtils.hashPassword(userData.getPassword());
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
      user.setPasswordHash(passwordUtils.hashPassword(data.getPassword()));
      userRepository.save(user);
    });
    return "done";
  }

  @Transactional
  public String seedPosts() {
    List<PostData> posts = postDataProvider.provide();
    // Random random = new Random();
    for (PostData pd : posts) {
      Integer authorId = pd.getAuthorId();
      Optional<User> userOpt = userRepository.findById(authorId);
      if (userOpt.isEmpty()) {
        continue;
      }
      User user = userOpt.get();
      Post post = new Post();
      post.setContent(pd.getContent());
      post.setAuthor(user);
      postRepository.save(post);
    }
    return "done";
  }
}
