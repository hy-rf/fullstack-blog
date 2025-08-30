package com.backend.service.dto.post;

import java.time.OffsetDateTime;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.SqlResultSetMapping;


@SqlResultSetMapping(name = "PostWithNumbersOfRepliesMapping",
    classes = @ConstructorResult(targetClass = PostWithNumbersOfRepliesDTO.class,
        columns = {@ColumnResult(name = "id", type = Integer.class),
            @ColumnResult(name = "content", type = String.class),
            @ColumnResult(name = "createdAt", type = OffsetDateTime.class),
            @ColumnResult(name = "authorId", type = Integer.class),
            @ColumnResult(name = "username", type = String.class),
            @ColumnResult(name = "userRoleNameList", type = String.class),
            @ColumnResult(name = "numberOfReplies", type = Integer.class)}))
public class PostWithNumbersOfRepliesDTO {
  private Integer id;
  private String content;
  private OffsetDateTime createdAt;
  private Integer authorId;
  private String username;
  private String userRoleNameList;
  private Integer numberOfReplies;

  public PostWithNumbersOfRepliesDTO(Integer id, String content, OffsetDateTime createdAt,
      Integer authorId, String username, String userRoleNameList, Integer numberOfReplies) {
    this.id = id;
    this.content = content;
    this.createdAt = createdAt;
    this.authorId = authorId;
    this.username = username;
    this.userRoleNameList = userRoleNameList;
    this.numberOfReplies = numberOfReplies;
  }

  // getters & setters
  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
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

  public Integer getAuthorId() {
    return authorId;
  }

  public void setAuthorId(Integer authorId) {
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

  public Integer getNumberOfReplies() {
    return numberOfReplies;
  }

  public void setNumberOfReplies(Integer numberOfReplies) {
    this.numberOfReplies = numberOfReplies;
  }
}
