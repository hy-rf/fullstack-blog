package com.backend.service;

import com.backend.dto.post.PostDTO;
import com.backend.dto.post.PostWithNumbersOfRepliesDTO;
import com.backend.dto.post.UpdatePostDto;
import com.backend.dto.post.UpdatePostResultDto;
import com.backend.dto.post.UpdatePostResultStatus;
import com.backend.mapper.PostMapper;
import com.backend.model.Post;
import com.backend.model.User;
import com.backend.repository.PostRepository;
import com.backend.repository.PostSpecification;
import com.backend.repository.UserRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.domain.Pageable;

@Service
public class PostService {

  private final PostMapper postMapper;
  private final PostRepository postRepository;
  private final UserRepository userRepository;

  private final JdbcTemplate jdbcTemplate;

  public PostService(PostMapper postMapper, PostRepository postRepository,
      UserRepository userRepository, JdbcTemplate jdbcTemplate) {
    this.postMapper = postMapper;
    this.postRepository = postRepository;
    this.userRepository = userRepository;
    this.jdbcTemplate = jdbcTemplate;
  }

  public Post createPost(String content, Long userId, Optional<Long> postId) {
    Optional<User> userOpt = userRepository.findById(userId);
    if (!userOpt.isPresent()) {
      throw new IllegalArgumentException("User not found with id: " + userId);
    }
    Post post = new Post();
    post.setContent(content);
    post.setAuthor(userOpt.get());
    if (postId.isPresent()) {
      Optional<Post> parentPost = postRepository.findById(postId.get());
      if (!parentPost.isPresent()) {
        throw new IllegalArgumentException("Post not found with id: " + postId);
      }
      post.setParentPost(parentPost.get());
    }
    return postRepository.save(post);
  }

  public List<Post> getPostsByUser(Long userId) {
    Optional<List<Post>> posts = postRepository.findByAuthorId(userId);
    if (posts.isEmpty() || posts.get().isEmpty()) {
      throw new IllegalArgumentException("No posts found for user with id: " + userId);
    }
    return posts.get();
  }

  public PostDTO getPostById(Long id) {
    Optional<Post> postOpt = postRepository.findById(id);
    if (postOpt.isEmpty()) {
      throw new Error();
    }
    Post post = postOpt.get();
    PostDTO postDTO = postMapper.toPostDTO(post, 99);
    return postDTO;
  }

  public Page<Post> getPosts(String keyword, String authorName, LocalDateTime createdAfter,
      LocalDateTime createdBefore, String sortBy, String order, int page, int size) {
    Specification<Post> spec = PostSpecification.hasTitleOrContentLike(keyword)
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

  public PostWithNumbersOfRepliesDTO findPostById(Long postId) {
    String sql =
        """
                SELECT
                  p.id,
                  p.content,
                  p.created_at,
                  u.id AS authorId,
                  u.username,
                  STRING_AGG(r.name, ', ') AS user_role_name_list,
                  COUNT(rep.id) AS number_of_replies
                FROM posts p
                LEFT JOIN users u ON p.author_id = u.id
                LEFT JOIN user_roles ur ON u.id = ur.user_id
                LEFT JOIN roles r ON ur.role_id = r.id
                LEFT JOIN reply rep ON rep.post_id = p.id
                WHERE p.id = ?
                GROUP BY p.id, p.title, p.content, p.created_at, p.updated_at, p.author_id, u.id, u.username
            """;

    return jdbcTemplate.queryForObject(sql, new Object[] {postId},
        (rs, rowNum) -> new PostWithNumbersOfRepliesDTO(rs.getLong("id"), rs.getString("content"),
            rs.getTimestamp("created_at").toInstant().atOffset(ZoneOffset.UTC),
            rs.getLong("authorId"), rs.getString("username"), rs.getString("user_role_name_list"),
            rs.getLong("number_of_replies")));
  }

}
