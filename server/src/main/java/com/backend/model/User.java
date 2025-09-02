package com.backend.model;

import com.backend.service.dto.post.AuthorDto;
import com.backend.service.dto.post.AuthorViewModel;
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

  @JsonIgnore
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

  @PrePersist
  public void onCreate() {
    createdAt = OffsetDateTime.now();
  }

  public AuthorDto toAuthorDto() {
    AuthorDto authorDto = new AuthorDto();
    authorDto.setId(this.getId());
    authorDto.setUsername(this.getUsername());
    authorDto.setRoles(this.getRoles());
    return authorDto;
  }

  public AuthorViewModel toAuthorViewModel() {
    AuthorViewModel authorViewModel = new AuthorViewModel();
    authorViewModel.setId(this.getId());
    authorViewModel.setUsername(this.getUsername());
    authorViewModel.setRoles(this.getRoles());
    return authorViewModel;
  }
}
