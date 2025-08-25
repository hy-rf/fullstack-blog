package com.backend.service;

import com.backend.dto.post.PostDTO;
import com.backend.dto.post.UpdatePostDto;
import com.backend.dto.post.UpdatePostResultDto;
import com.backend.dto.post.UpdatePostResultStatus;
import com.backend.mapper.PostMapper;
import com.backend.model.Post;
import com.backend.model.Reply;
import com.backend.model.User;
import com.backend.repository.PostRepository;
import com.backend.repository.PostSpecification;
import com.backend.repository.ReplyRepository;
import com.backend.repository.UserRepository;

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

@Service
public class PostService {

  private final PostMapper postMapper;
  private final PostRepository postRepository;
  private final ReplyRepository replyRepository;
  private final UserRepository userRepository;

  public PostService(PostMapper postMapper, PostRepository postRepository, ReplyRepository replyRepository,
      UserRepository userRepository) {
    this.postMapper = postMapper;
    this.postRepository = postRepository;
    this.replyRepository = replyRepository;
    this.userRepository = userRepository;
  }

  public List<Post> getAllPosts() {
    return postRepository.findAll();
  }

  public Post createPost(String title, String content, Long userId) {
    Optional<User> userOpt = userRepository.findById(userId);
    if (!userOpt.isPresent()) {
      throw new IllegalArgumentException("User not found with id: " + userId);
    }
    Post post = new Post();
    post.setTitle(title);
    post.setContent(content);
    post.setAuthor(userOpt.get());
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

  public Page<Post> getPosts(
      String keyword,
      String authorName,
      LocalDateTime createdAfter,
      LocalDateTime createdBefore,
      String sortBy,
      String order, int page,
      int size) {
    Specification<Post> spec = PostSpecification.hasTitleOrContentLike(keyword)
        .and(PostSpecification.hasAuthorNameLike(authorName))
        .and(PostSpecification.createdAfter(createdAfter))
        .and(PostSpecification.createdBefore(createdBefore));

    Sort.Direction direction = order.equalsIgnoreCase("asc") ? Sort.Direction.ASC : Sort.Direction.DESC;
    Pageable pageable = PageRequest.of(page - 1, size, Sort.by(direction, sortBy));
    Page<Post> postPage = postRepository.findAll(spec, pageable);
    return postPage;
  }

  @Transactional
  public String createReply(Long userId, String content, Optional<Long> postId, Optional<Long> parentReplyId) {
    if (!postId.isPresent())
      throw new IllegalArgumentException("No post id!");
    Optional<Post> postOpt = postRepository.findById(postId.get());
    if (!postOpt.isPresent()) {
      throw new IllegalArgumentException("Post not found with id: " + postId);
    }
    Post post = postOpt.get();
    Reply reply = new Reply();
    reply.setContent(content);
    reply.setPost(post); // Set parent post
    reply.setAuthor(userRepository.findById(userId)
        .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId)));
    post.getReplies().add(reply);
    if (parentReplyId.isPresent()) {
      Optional<Reply> parentReplyOpt = replyRepository.findById(parentReplyId);
      if (!parentReplyOpt.isPresent()) {
        throw new IllegalArgumentException("Parent reply not found with id: " + parentReplyId);
      }
      Reply parentReply = parentReplyOpt.get();
      reply.setParentReply(parentReply);
      parentReply.getReplies().add(reply);
      replyRepository.save(parentReply);
    }
    replyRepository.save(reply);
    postRepository.save(post);
    return "Done.";
  }

  public List<Reply> getRepliesByPostId(Long postId) {
    Optional<Post> postOpt = postRepository.findById(postId);
    if (!postOpt.isPresent()) {
      throw new IllegalArgumentException("Post not found with id: " + postId);
    }
    Post post = postOpt.get();
    return replyRepository.findByPostId(post.getId());
  }

  public List<Reply> getRepliesByParentReplyId(Long parentReplyId) {
    Optional<Reply> parentReplyOpt = replyRepository.findById(parentReplyId);
    if (!parentReplyOpt.isPresent()) {
      throw new IllegalArgumentException("Parent reply not found with id: " + parentReplyId);
    }
    Reply parentReply = parentReplyOpt.get();
    return replyRepository.findByParentReplyId(parentReply.getId());
  }

  public UpdatePostResultDto UpdatePost(UpdatePostDto updatePostDto) {
    Optional<Post> postToUpdateOpt = postRepository.findById(updatePostDto.getPostId());
    if (postToUpdateOpt.isEmpty())
      return new UpdatePostResultDto(UpdatePostResultStatus.POST_NOT_FOUND, "Post not found");
    Post postToUpdate = postToUpdateOpt.get();
    if (!postToUpdate.getAuthor().getId().equals(updatePostDto.getAuthorId()))
      return new UpdatePostResultDto(UpdatePostResultStatus.AUTHOR_UNMATCHED, "Author unmatched");
    postToUpdate.setTitle(updatePostDto.getTitle());
    postToUpdate.setContent(updatePostDto.getContent());
    postRepository.save(postToUpdate);
    return new UpdatePostResultDto(UpdatePostResultStatus.SUCCESS, "success");
  }
}
