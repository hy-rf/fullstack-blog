package com.backend.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "avatars")
public class Avatar {

  @Column(name = "user_id")
  private Integer userId;

  @Id
  @Column(name = "url")
  private String url;

  @Column(name = "created_at")
  private OffsetDateTime createdAt;

  @PrePersist
  public void onCreate() {
    createdAt = OffsetDateTime.now();
  }
}
