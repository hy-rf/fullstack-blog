package com.backend.dto.post;

import java.time.OffsetDateTime;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.Entity;
import jakarta.persistence.SqlResultSetMapping;


@SqlResultSetMapping(name = "PostWithNumbersOfRepliesMapping",
    classes = @ConstructorResult(targetClass = PostWithNumbersOfRepliesDTO.class,
        columns = {@ColumnResult(name = "id", type = Long.class),
            @ColumnResult(name = "title", type = String.class),
            @ColumnResult(name = "content", type = String.class),
            @ColumnResult(name = "createdAt", type = OffsetDateTime.class),
            @ColumnResult(name = "updatedAt", type = OffsetDateTime.class),
            @ColumnResult(name = "authorId", type = Long.class),
            @ColumnResult(name = "username", type = String.class),
            @ColumnResult(name = "userRoleNameList", type = String.class),
            @ColumnResult(name = "numberOfReplies", type = Long.class)}))
public class PostWithNumbersOfRepliesDTO {
  private Long id;
  private String title;
  private String content;
  private OffsetDateTime createdAt;
  private OffsetDateTime updatedAt;
  private Long authorId;
  private String username;
  private String userRoleNameList;
  private Long numberOfReplies;

  public PostWithNumbersOfRepliesDTO(Long id, String title, String content,
      OffsetDateTime createdAt, OffsetDateTime updatedAt, Long authorId, String username,
      String userRoleNameList, Long numberOfReplies) {
    this.id = id;
    this.title = title;
    this.content = content;
    this.createdAt = createdAt;
    this.updatedAt = updatedAt;
    this.authorId = authorId;
    this.username = username;
    this.userRoleNameList = userRoleNameList;
    this.numberOfReplies = numberOfReplies;
  }

  // getters & setters
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  public String getContent() {
    return content;
  }

  public void setContent(String content) {
    this.content = content;
  }

  public OffsetDateTime getCreatedAt() {
    return createdAt;
  }

  public void setCreatedAt(OffsetDateTime createdAt) {
    this.createdAt = createdAt;
  }

  public OffsetDateTime getUpdatedAt() {
    return updatedAt;
  }

  public void setUpdatedAt(OffsetDateTime updatedAt) {
    this.updatedAt = updatedAt;
  }

  public Long getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Long authorId) {
    this.authorId = authorId;
  }

  public String getusername() {
    return username;
  }

  public void setusername(String username) {
    this.username = username;
  }

  public String getUserRoleNameList() {
    return userRoleNameList;
  }

  public void setUserRoleNameList(String userRoleNameList) {
    this.userRoleNameList = userRoleNameList;
  }

  public Long getNumberOfReplies() {
    return numberOfReplies;
  }

  public void setNumberOfReplies(Long numberOfReplies) {
    this.numberOfReplies = numberOfReplies;
  }
}
