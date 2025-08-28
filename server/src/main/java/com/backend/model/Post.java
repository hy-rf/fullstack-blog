package com.backend.model;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "posts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Post {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(length = 500)
  private String content;

  @Column(name = "created_at")
  private OffsetDateTime createdAt;

  @ManyToOne
  @JoinColumn(name = "post_id")
  private Post parentPost;

  @JsonIgnore
  @OneToMany(mappedBy = "parentPost", cascade = CascadeType.PERSIST)
  private List<Post> posts = new ArrayList<>();

  @ManyToOne
  @JoinColumn(name = "author_id", referencedColumnName = "id")
  private User author;

  @PrePersist
  public void onCreate() {
    createdAt = OffsetDateTime.now();
  }
}
