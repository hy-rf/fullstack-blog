package com.backend.controller;

import com.backend.common.AccessTokenData;
import com.backend.controller.dto.post.AddReactionRequest;
import com.backend.controller.dto.post.CreatePostRequest;
import com.backend.controller.dto.post.PostSummary;
import com.backend.controller.dto.post.UpdatePostRequest;
import com.backend.controller.dto.post.UpdatePostResponse;
import com.backend.dao.dto.PostPage;
import com.backend.service.PostService;
import com.backend.service.UploadService;
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
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequiredArgsConstructor
public class PostController {

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
      description = "offset",
      example = "0",
      schema = @Schema(defaultValue = "0")
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
      ((AccessTokenData) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getId();
    CreatePostCommand createPostCommand = new CreatePostCommand(
      createPostRequest.getContent(),
      userId,
      createPostRequest.getRootPostId(),
      createPostRequest.getParentPostId()
    );
    CreatePostCommandResult result = postService.createPost(createPostCommand);
    createPostRequest
      .getImagesBase64Strings()
      .forEach(f -> {
        /**
         * TODO: extract processing of raw base64 string
         */
        String[] fileStrings = f.split(",");
        byte[] file = Base64.getDecoder().decode(
          fileStrings[1].getBytes(StandardCharsets.UTF_8)
        );
        try {
          uploadService.save(
            file,
            "post_image",
            result.getId(),
            fileStrings[0].split(";")[0].split("/")[1]
          );
        } catch (IOException e) {
          log.error(e.getLocalizedMessage());
        }
      });
    if (!result.isSuccess()) return ResponseEntity.badRequest().body(
      result.getId()
    );
    return ResponseEntity.ok().body(result.getId());
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

  /**
   * Search for posts
   * @param keyword
   * @param authorName
   * @param createdAfter
   * @param createdBefore
   * @param sortBy
   * @param order
   * @param page
   * @param size
   * @return
   */
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
      ((AccessTokenData) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getId();
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

  @PostMapping("/reaction")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<Boolean> addReaction(
    @RequestBody AddReactionRequest addLikeRequest
  ) {
    Integer postId = addLikeRequest.getPostId();
    Integer userId =
      ((AccessTokenData) (SecurityContextHolder.getContext().getAuthentication()).getPrincipal()).getId();
    log.info(String.format("%d%d", postId, userId));
    return ResponseEntity.ok(true);
  }
}
