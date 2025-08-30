package com.backend.controller;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import com.backend.controller.dto.post.CreatePostRequest;
import com.backend.controller.dto.post.PostSummary;
import com.backend.controller.dto.post.UpdatePostRequest;
import com.backend.controller.dto.post.UpdatePostResponse;
import com.backend.mapper.PostMapper;
import com.backend.model.Post;
import com.backend.security.CustomUserDetails;
import com.backend.service.PostService;
import com.backend.service.dto.post.GetPostByIdCommand;
import com.backend.service.dto.post.PostDTO;
import com.backend.service.dto.post.UpdatePostDto;
import com.backend.service.dto.post.UpdatePostResultDto;
import com.backend.service.dto.post.UpdatePostResultStatus;

@Slf4j
@RestController
public class PostController {

    private final PostMapper postMapper;
    private final PostService postService;

    public PostController(PostMapper postMapper, PostService postService) {
        this.postMapper = postMapper;
        this.postService = postService;
    }

    @GetMapping("/post")
    public ResponseEntity<List<PostSummary>> getFeed(@RequestParam String sort_by,
            @RequestParam String page_token) {
        List<PostSummary> posts = postService.getPosts(sort_by);
        return ResponseEntity.ok().body(posts);
    }

    @PostMapping("/post")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<String> createPost(@RequestBody CreatePostRequest createPostRequest,
            HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        Integer userId = userDetails.getId();
        postService.createPost(createPostRequest.getContent(), userId,
                createPostRequest.getPostId());
        return ResponseEntity.ok()
                .body("Successfully created post with title: " + createPostRequest.getContent());
    }

    @GetMapping("/me/posts")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<List<Post>> getPostsByUser(HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getId();
        List<Post> posts = postService.getPostsByUser(userId);
        return ResponseEntity.ok().body(posts);
    }

    @GetMapping("/post/{id}")
    public ResponseEntity<PostDTO> getPostById(@PathVariable Integer id,
            HttpServletResponse response) {
        PostDTO post = postService.getPostById(new GetPostByIdCommand(id));
        if (post == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return null;
        }
        return ResponseEntity.ok().body(post);
    }

    // 3 db queries
    @GetMapping("/posts/search")
    public Page<PostSummary> getPosts(@RequestParam(required = false) String keyword,
            @RequestParam(required = false) String authorName,
            @RequestParam(required = false) @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdAfter,
            @RequestParam(required = false) @DateTimeFormat(
                    iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime createdBefore,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String order,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int size) {
        if (size > 50)
            size = 50;
        Page<Post> postPage = postService.getPosts(keyword, authorName, createdAfter, createdBefore,
                sortBy, order, page, size);
        Page<PostSummary> postListPage = postPage.map(postMapper::toPostSummary);
        return postListPage;
    }

    @PutMapping("/post")
    @PreAuthorize("isAuthenticated()")
    public ResponseEntity<UpdatePostResponse> updatePost(
            @Valid @RequestBody UpdatePostRequest updatePostRequest) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Integer userId = userDetails.getId();
        UpdatePostDto updatePostDto = new UpdatePostDto(updatePostRequest.getPostId(), userId,
                updatePostRequest.getContent());
        UpdatePostResultDto updatePostResultDto = postService.UpdatePost(updatePostDto);
        if (updatePostResultDto.getUpdatePostResultStatus()
                .equals(UpdatePostResultStatus.POST_NOT_FOUND)) {
            return ResponseEntity.badRequest().body(new UpdatePostResponse(false, "error"));
        }
        if (updatePostResultDto.getUpdatePostResultStatus()
                .equals(UpdatePostResultStatus.AUTHOR_UNMATCHED)) {
            return ResponseEntity.badRequest().body(new UpdatePostResponse(false, "error"));
        }
        if (updatePostResultDto.getUpdatePostResultStatus().equals(UpdatePostResultStatus.ERROR)) {
            return ResponseEntity.badRequest().body(new UpdatePostResponse(false, "error"));
        }
        return ResponseEntity.ok()
                .body(new UpdatePostResponse(true, updatePostResultDto.getMessage()));
    }
}
