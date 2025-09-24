package com.backend.controller;

import com.backend.controller.dto.post.AddLikeRequest;
import com.backend.controller.dto.post.AddLikeResponse;
import com.backend.controller.dto.post.CreatePostRequest;
import com.backend.controller.dto.post.PostSummary;
import com.backend.controller.dto.post.UpdatePostRequest;
import com.backend.controller.dto.post.UpdatePostResponse;
import com.backend.repository.PostRepository;
import com.backend.repository.dto.PostPage;
import com.backend.security.CustomUserDetails;
import com.backend.service.PostService;
import com.backend.service.UploadService;
import com.backend.service.dto.post.CreateLikeCommand;
import com.backend.service.dto.post.CreatePostCommand;
import com.backend.service.dto.post.CreatePostCommandResult;
import com.backend.service.dto.post.GetPostByIdCommand;
import com.backend.service.dto.post.UpdatePostDto;
import com.backend.service.dto.post.UpdatePostResultDto;
import com.backend.service.dto.post.UpdatePostResultStatus;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

  private final PostRepository postRepository;
  private final PostService postService;
  private final UploadService uploadService;

  /**
   * This is for getting feeds on home page and it gets different posts if page_token exists.
   *
   * @param page_token The token with pagination or other info.
   * @return
   */
  @GetMapping("/post")
  public ResponseEntity<List<PostSummary>> getFeed(
    @Parameter(
      description = "Page token for pagination",
      example = "default_page_token",
      schema = @Schema(defaultValue = "default_page_token")
    ) @RequestParam Integer offset
  ) {
    List<PostSummary> posts = postService.getPosts(offset);
    return ResponseEntity.ok(posts);
  }

  /**
   * Create post or a child post.
   *
   * @param createPostRequest
   * @param response
   * @return
   */
  @PostMapping("/post")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Integer> createPost(
    @RequestBody CreatePostRequest createPostRequest,
    HttpServletResponse response
  ) {
    Integer userId =
      ((CustomUserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getId();
    CreatePostCommand createPostCommand = new CreatePostCommand(
      createPostRequest.getContent(),
      userId,
      createPostRequest.getRootPostId(),
      createPostRequest.getPostId()
    );
    CreatePostCommandResult result = postService.createPost(createPostCommand);
    if (!result.isSuccess()) return ResponseEntity.badRequest().body(
      result.getId()
    );
    return ResponseEntity.ok().body(result.getId());
  }

  @PostMapping("/post-image")
  @PreAuthorize("hasRole('user')")
  public ResponseEntity<String> uploadAvatar(
    @RequestParam("file") MultipartFile file,
    @RequestParam Integer postId
  ) throws IOException {
    final long MAX_FILE_SIZE = 25 * 1024 * 1024;
    if (file.getSize() > MAX_FILE_SIZE) {
      return ResponseEntity.badRequest().body("Too big");
    }
    String contentType = file.getContentType();
    if (contentType == null) {
      return ResponseEntity.badRequest().body("Unknown file");
    }
    if (!contentType.startsWith("image/")) {
      return ResponseEntity.badRequest().body("Not image");
    }
    uploadService.save(file, "post_image", Integer.valueOf(postId));
    return ResponseEntity.ok("File uploaded successfully.");
  }

  @GetMapping("/post/{id}")
  public ResponseEntity<List<PostPage>> getPostById(
    @PathVariable Integer id,
    HttpServletResponse response
  ) {
    List<PostPage> posts = postService.getPostAndChildPostsByRootPostId(
      new GetPostByIdCommand(id)
    );
    return ResponseEntity.ok().body(posts);
  }

  // 3 db queries
  @GetMapping("/posts/search")
  public PageImpl<PostSummary> getPosts(
    @RequestParam(required = false) String keyword,
    @RequestParam(required = false) String authorName,
    @RequestParam(required = false) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE_TIME
    ) LocalDateTime createdAfter,
    @RequestParam(required = false) @DateTimeFormat(
      iso = DateTimeFormat.ISO.DATE_TIME
    ) LocalDateTime createdBefore,
    @RequestParam(defaultValue = "createdAt") String sortBy,
    @RequestParam(defaultValue = "desc") String order,
    @RequestParam(defaultValue = "1") int page,
    @RequestParam(defaultValue = "10") int size
  ) {
    if (size > 50) size = 50;
    PageImpl<PostSummary> postPage = postService.getPosts(
      keyword,
      authorName,
      createdAfter,
      createdBefore,
      sortBy,
      order,
      page,
      size
    );
    return postPage;
  }

  /**
   * Update post
   *
   * TODO: Add edit history
   *
   * @param updatePostRequest
   * @return
   */
  @PutMapping("/post")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UpdatePostResponse> updatePost(
    @Valid @RequestBody UpdatePostRequest updatePostRequest
  ) {
    Integer userId =
      ((CustomUserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getId();
    UpdatePostDto updatePostDto = new UpdatePostDto(
      updatePostRequest.getPostId(),
      userId,
      updatePostRequest.getContent()
    );
    UpdatePostResultDto updatePostResultDto = postService.UpdatePost(
      updatePostDto
    );
    if (
      updatePostResultDto
        .getUpdatePostResultStatus()
        .equals(UpdatePostResultStatus.POST_NOT_FOUND)
    ) {
      return ResponseEntity.badRequest().body(
        new UpdatePostResponse(false, "error")
      );
    }
    if (
      updatePostResultDto
        .getUpdatePostResultStatus()
        .equals(UpdatePostResultStatus.AUTHOR_UNMATCHED)
    ) {
      return ResponseEntity.badRequest().body(
        new UpdatePostResponse(false, "error")
      );
    }
    if (
      updatePostResultDto
        .getUpdatePostResultStatus()
        .equals(UpdatePostResultStatus.ERROR)
    ) {
      return ResponseEntity.badRequest().body(
        new UpdatePostResponse(false, "error")
      );
    }
    return ResponseEntity.ok().body(
      new UpdatePostResponse(true, updatePostResultDto.getMessage())
    );
  }

  @PostMapping("/like")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<AddLikeResponse> addLike(
    @RequestBody AddLikeRequest addLikeRequest
  ) {
    Integer postId = addLikeRequest.getPostId();
    Authentication authentication =
      SecurityContextHolder.getContext().getAuthentication();
    CustomUserDetails userDetails =
      (CustomUserDetails) authentication.getPrincipal();
    Integer userId = userDetails.getId();
    postService.createLike(new CreateLikeCommand(postId, userId));
    return ResponseEntity.ok(new AddLikeResponse(true));
  }

  @DeleteMapping("/like")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<AddLikeResponse> removeLike(
    @RequestBody AddLikeRequest addLikeRequest
  ) {
    Integer postId = addLikeRequest.getPostId();
    Integer userId =
      ((CustomUserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getId();
    postService.createLike(new CreateLikeCommand(postId, userId));
    return ResponseEntity.ok(new AddLikeResponse(true));
  }

  @PostMapping("/save-post")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Boolean> savePost(
    @RequestParam(name = "post-id") Integer postId
  ) {
    Integer userId =
      ((CustomUserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getId();
    int result = postRepository.addSavedPost(userId, postId);
    if (result == 0) return ResponseEntity.status(403).body(false);
    if (result == 1) return ResponseEntity.ok(true);
    if (result > 1) {
      return ResponseEntity.status(500).body(false);
    } else {
      return ResponseEntity.status(500).body(false);
    }
  }

  @DeleteMapping("/save-post")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Boolean> removeSavedPost(@RequestParam Integer postId) {
    Integer userId =
      ((CustomUserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getId();
    int result = postRepository.removeSavedPost(userId, postId);
    if (result == 0) return ResponseEntity.status(403).body(false);
    if (result == 1) return ResponseEntity.ok(true);
    if (result > 1) {
      return ResponseEntity.status(500).body(false);
    } else {
      return ResponseEntity.status(500).body(false);
    }
  }

  @GetMapping("/saved")
  public ResponseEntity<List<PostSummary>> getSavedPosts() {
    Integer userId =
      ((CustomUserDetails) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getId();
    return ResponseEntity.ok(postRepository.getSavedPostsByUserId(userId));
  }
}
