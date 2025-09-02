package com.backend.service;

import com.backend.controller.dto.post.PostSummary;
import com.backend.model.Post;
import com.backend.model.User;
import com.backend.repository.PostRepository;
import com.backend.repository.PostSpecification;
import com.backend.repository.UserRepository;
import com.backend.repository.dto.PostPage;
import com.backend.service.dto.post.CreatePostCommand;
import com.backend.service.dto.post.CreatePostCommandResult;
import com.backend.service.dto.post.GetPostByIdCommand;
import com.backend.service.dto.post.UpdatePostDto;
import com.backend.service.dto.post.UpdatePostResultDto;
import com.backend.service.dto.post.UpdatePostResultStatus;
import lombok.extern.slf4j.Slf4j;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

@Slf4j
@Service
public class PostService {

  private final PostRepository postRepository;
  private final UserRepository userRepository;

  public PostService(PostRepository postRepository, UserRepository userRepository) {
    this.postRepository = postRepository;
    this.userRepository = userRepository;
  }

  /**
   * Provide feed posts
   * 
   * @return
   */
  public List<PostSummary> getPosts(Integer offset) {
    List<PostSummary> p = postRepository.findAllByPostSummariesAndOffset(offset, null);
    return p;
  }

  /**
   * Create post sql: insert into posts (author_id, content, created_at, post_id, root_post_id)
   * values (?, ?, ?, ?, ?)
   * 
   * @param content
   * @param userId
   * @param postId
   * @return
   */
  @Transactional
  public CreatePostCommandResult createPost(CreatePostCommand createPostCommand) {
    String content = createPostCommand.getContent();
    Integer userId = createPostCommand.getAuthorId();
    Optional<Integer> rootPostId = createPostCommand.getRootPostId();
    Optional<Integer> parentPostId = createPostCommand.getParentPostId();
    User author = userRepository.getReferenceById(userId);
    Post post = new Post();
    post.setContent(content);
    post.setAuthor(author);
    if (rootPostId.isPresent()) {
      Post rootPost = postRepository.getReferenceById(rootPostId.get());
      post.setRootPost(rootPost);
    }
    if (parentPostId.isPresent()) {
      Post parentPost = postRepository.getReferenceById(parentPostId.get());
      post.setParentPost(parentPost);
    }
    postRepository.save(post);
    return new CreatePostCommandResult(true);
  }

  public List<Post> getPostsByUser(Integer userId) {
    Optional<List<Post>> posts = postRepository.findByAuthorId(userId);
    if (posts.isEmpty() || posts.get().isEmpty()) {
      throw new IllegalArgumentException("No posts found for user with id: " + userId);
    }
    return posts.get();
  }

  /**
   * This is getting child posts of current post so no need to get post content of current post.
   * TODO: filter child posts
   * 
   * @param getPostByIdCommand
   * @return
   */
  public List<PostPage> getPostAndChildPostsByRootPostId(GetPostByIdCommand getPostByIdCommand) {
    List<PostPage> posts =
        postRepository.findAllByRootPostIdOrderByCreatedAtDesc(getPostByIdCommand.getId());
    return posts;
  }

  public Page<Post> getPosts(String keyword, String authorName, LocalDateTime createdAfter,
      LocalDateTime createdBefore, String sortBy, String order, int page, int size) {
    Specification<Post> spec =
        PostSpecification.hasTitleOrContentLike(keyword).and(PostSpecification.parentPostIsNull())
            .and(PostSpecification.hasAuthorNameLike(authorName))
            .and(PostSpecification.createdAfter(createdAfter))
            .and(PostSpecification.createdBefore(createdBefore));

    Sort.Direction direction =
        order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortBy));
    Page<Post> postPage = postRepository.findAll(spec, pageable);
    return postPage;
  }

  public UpdatePostResultDto UpdatePost(UpdatePostDto updatePostDto) {
    Optional<Post> postToUpdateOpt = postRepository.findById(updatePostDto.getPostId());
    if (postToUpdateOpt.isEmpty())
      return new UpdatePostResultDto(UpdatePostResultStatus.POST_NOT_FOUND, "Post not found");
    Post postToUpdate = postToUpdateOpt.get();
    if (!postToUpdate.getAuthor().getId().equals(updatePostDto.getAuthorId()))
      return new UpdatePostResultDto(UpdatePostResultStatus.AUTHOR_UNMATCHED, "Author unmatched");
    postToUpdate.setContent(updatePostDto.getContent());
    postRepository.save(postToUpdate);
    return new UpdatePostResultDto(UpdatePostResultStatus.SUCCESS, "success");
  }

  // public PostWithNumbersOfRepliesDTO findPostById(Integer postId) {
  // String sql =
  // """
  // SELECT
  // p.id,
  // p.content,
  // p.created_at,
  // u.id AS authorId,
  // u.username,
  // STRING_AGG(r.name, ', ') AS user_role_name_list,
  // COUNT(rep.id) AS number_of_replies
  // FROM posts p
  // LEFT JOIN users u ON p.author_id = u.id
  // LEFT JOIN user_roles ur ON u.id = ur.user_id
  // LEFT JOIN roles r ON ur.role_id = r.id
  // LEFT JOIN reply rep ON rep.post_id = p.id
  // WHERE p.id = ?
  // GROUP BY p.id, p.title, p.content, p.created_at, p.updated_at, p.author_id, u.id, u.username
  // """;

  // return jdbcTemplate.queryForObject(sql, new Object[] {postId},
  // (rs, rowNum) -> new PostWithNumbersOfRepliesDTO(rs.getInteger("id"), rs.getString("content"),
  // rs.getTimestamp("created_at").toInstant().atOffset(ZoneOffset.UTC),
  // rs.getInteger("authorId"), rs.getString("username"), rs.getString("user_role_name_list"),
  // rs.getInteger("number_of_replies")));
  // }

}
