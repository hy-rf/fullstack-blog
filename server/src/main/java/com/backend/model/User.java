package com.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(unique = true, length = 50)
  private String username;

  @Column(unique = true, length = 100)
  private String email;

  @JsonIgnore
  @Column(name = "password_hash", columnDefinition = "bpchar(60)")
  private String passwordHash;

  @Column(name = "created_at")
  private OffsetDateTime createdAt = OffsetDateTime.now();

  @ManyToMany(fetch = FetchType.EAGER)
  @JsonManagedReference
  @JoinTable(
    name = "user_roles",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "role_id")
  )
  private List<Role> roles = new ArrayList<>();

  @ManyToMany(fetch = FetchType.LAZY)
  @JsonManagedReference
  @JoinTable(
    name = "followings",
    joinColumns = @JoinColumn(name = "follower_id"),
    inverseJoinColumns = @JoinColumn(name = "followee_id")
  )
  private List<User> followings = new ArrayList<>();

  @ManyToMany
  @JoinTable(
    name = "user_saved_posts",
    joinColumns = @JoinColumn(name = "user_id"),
    inverseJoinColumns = @JoinColumn(name = "post_id")
  )
  private List<Post> savedPosts = new ArrayList<>();

  @PrePersist
  public void onCreate() {
    createdAt = OffsetDateTime.now();
  }
}
