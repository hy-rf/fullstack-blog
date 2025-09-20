package com.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "post_history")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostHistory {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "post_id", referencedColumnName = "id")
  private Post post;

  private String content;

  @Column(name = "image_urls", columnDefinition = "text[]")
  private List<Integer> imageUrls;

  @Column(name = "created_at")
  private OffsetDateTime createdAt;

  @PrePersist
  public void onCreate() {
    createdAt = OffsetDateTime.now();
  }
}
