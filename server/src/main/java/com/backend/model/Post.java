package com.backend.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @Column(length = 1000)
  private String content;

  @Column(name = "created_at")
  private OffsetDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "author_id", referencedColumnName = "id")
  private User author;

  @ManyToOne(optional = true)
  @JoinColumn(name = "root_post_id")
  private Post rootPost;

  @ManyToOne(optional = true)
  @JoinColumn(name = "parent_post_id")
  private Post parentPost;

  private Integer likeCount;
  private Integer saveCount;
  private Integer postCount;

  @JsonIgnore
  @OneToMany(mappedBy = "rootPost", cascade = CascadeType.PERSIST)
  private List<Post> posts = new ArrayList<>();

  @ManyToMany
  @JoinTable(
    name = "post_likes",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "user_id")
  )
  private Set<User> likes = new HashSet<>();

  @ManyToMany
  @JoinTable(
    name = "post_tags",
    joinColumns = @JoinColumn(name = "post_id"),
    inverseJoinColumns = @JoinColumn(name = "tag_id")
  )
  private Set<Tag> tags = new HashSet<>();

  @OneToMany(mappedBy = "post")
  private List<PostImage> postImages = new ArrayList<>();

  @PrePersist
  public void onCreate() {
    createdAt = OffsetDateTime.now();
    likeCount = 0;
    saveCount = 0;
    postCount = 0;
  }
}
