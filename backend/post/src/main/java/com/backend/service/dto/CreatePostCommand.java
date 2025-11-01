package com.backend.service.dto;

import java.util.Optional;
import lombok.Getter;

/**
 * This class serves as type of parameter of
 * {@link com.backend.service.PostService#createPost(CreatePostCommand)}.
 *
 * <p>
 * It encapsulates the content of a post, the author, and optionally the root and parent post IDs
 * for threading.
 * </p>
 *
 * @see com.backend.service.PostService#createPost(CreatePostCommand)
 *
 * @see com.backend.service.dto.CreatePostCommandResult
 */
@Getter
public class CreatePostCommand {

  private final String content;
  private final Integer authorId;
  private final Optional<Integer> rootPostId;
  private final Optional<Integer> parentPostId;

  /**
   * Constructs a new {@code CreatePostCommand}.
   *
   * @param content the textual content of the post
   * @param authorId the ID of the author creating the post
   * @param rootPostId the ID of the root post if this post belongs to a post(thread), otherwise
   *        {@code null}
   * @param parentPostId the ID of the direct parent post if this is a child post, otherwise
   *        {@code null}
   */
  public CreatePostCommand(
    String content,
    Integer authorId,
    Optional<Integer> rootPostId,
    Optional<Integer> parentPostId
  ) {
    this.content = content;
    this.authorId = authorId;
    this.rootPostId = rootPostId;
    this.parentPostId = parentPostId;
  }
}
