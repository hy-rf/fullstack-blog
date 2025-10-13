package com.backend.service;

import com.backend.controller.dto.post.PostSummary;
import com.backend.model.Post;
import com.backend.model.PostHistory;
import com.backend.model.User;
import com.backend.repository.PostHistoryRepository;
import com.backend.repository.PostRepository;
import com.backend.repository.UserRepository;
import com.backend.repository.dto.PostPage;
import com.backend.service.dto.post.CreateLikeCommand;
import com.backend.service.dto.post.CreatePostCommand;
import com.backend.service.dto.post.CreatePostCommandResult;
import com.backend.service.dto.post.GetPostByIdCommand;
import com.backend.service.dto.post.RemoveLikeCommand;
import com.backend.service.dto.post.UpdatePostDto;
import com.backend.service.dto.post.UpdatePostResultDto;
import com.backend.service.dto.post.UpdatePostResultStatus;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

  private final PostRepository postRepository;
  private final PostHistoryRepository postHistoryRepository;
  private final UserRepository userRepository;
  private final NamedParameterJdbcTemplate jdbc;

  /**
   * Provide feed posts
   *
   * @return
   */
  public List<PostSummary> getPosts(Integer offset) {
    List<PostSummary> p = postRepository.findAllByPostSummariesAndOffset(
      offset,
      50
    );
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
  public CreatePostCommandResult createPost(
    CreatePostCommand createPostCommand
  ) {
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
    Post p = postRepository.save(post);
    PostHistory postHistory = new PostHistory();
    postHistory.setPost(p);
    postHistory.setContent(p.getContent());
    postHistory.setImageUrls(
      p
        .getPostImages()
        .stream()
        .map(e -> e.getId())
        .toList()
    );
    postHistoryRepository.save(postHistory);
    return new CreatePostCommandResult(true, p.getId());
  }

  /**
   * This is getting child posts of current post so no need to get post content of current post.
   * TODO: filter child posts
   *
   * @param getPostByIdCommand
   * @return
   */
  public List<PostPage> getPostAndChildPostsByRootPostId(
    GetPostByIdCommand getPostByIdCommand
  ) {
    List<PostPage> posts =
      postRepository.findAllByRootPostIdOrderByCreatedAtDesc(
        getPostByIdCommand.getId()
      );
    return posts;
  }

  public PageImpl<PostSummary> getPosts(
    String keyword,
    String authorName,
    LocalDateTime createdAfter,
    LocalDateTime createdBefore,
    String sortBy,
    String order,
    int page,
    int size
  ) {
    String baseSelect = """
          SELECT
        p.id,
        p.content,
        p.created_at,
        p.author_id,
        u.username,
        (SELECT COUNT(*) FROM posts pc WHERE pc.parent_post_id = p.id) AS post_count,
        (SELECT COUNT(*) FROM post_likes l WHERE l.post_id = p.id) AS like_count,
        (SELECT COUNT(*) FROM user_saved_posts usp WHERE usp.post_id = p.id) AS save_count,
        (SELECT STRING_AGG(t.name, ', ') FROM post_tags pt JOIN tags t ON pt.tag_id = t.id WHERE pt.post_id = p.id) AS tags,
        (SELECT STRING_AGG(pi.url, ',') FROM post_images pi WHERE pi.post_id = p.id) AS urls
        FROM posts p
      JOIN users u ON u.id = p.author_id
          """;
    List<String> where = new ArrayList<>();

    MapSqlParameterSource params = new MapSqlParameterSource();

    if (keyword != null && !keyword.isBlank()) {
      // where.add("tsv @@ plainto_tsquery('english', :contentKeyword)");
      // params.addValue("contentKeyword", keyword.toLowerCase());
      where.add("content LIKE :contentKeyword");
      params.addValue("contentKeyword", "%" + keyword.toLowerCase() + "%");
    }
    // Its not open to the public right now
    // if (authorName != null && !authorName.isBlank()) {
    //   where.add("LOWER(u.username) LIKE :authorNameKeyword ESCAPE ''");
    //   params.addValue(
    //     "authorNameKeyword",
    //     "%" + authorName.toLowerCase() + "%"
    //   );
    // }

    // Sort by time range and not open to public
    // if (createdAfter != null) {
    //   where.add("p.created_at >= :createdAfter");
    //   params.addValue("createdAfter", java.sql.Timestamp.valueOf(createdAfter));
    // }

    // if (createdBefore != null) {
    //   where.add("p.created_at <= :createdBefore");
    //   params.addValue(
    //     "createdBefore",
    //     java.sql.Timestamp.valueOf(createdBefore)
    //   );
    // }

    StringBuilder sql = new StringBuilder(baseSelect);
    if (!where.isEmpty()) {
      sql.append(" WHERE ").append(String.join(" AND ", where));
    }

    // Shit start
    String sortColumn = "p.created_at";
    if (
      "createdAt".equalsIgnoreCase(sortBy) ||
      "created_at".equalsIgnoreCase(sortBy)
    ) {
      sortColumn = "p.created_at";
    } else if (
      "likeCount".equalsIgnoreCase(sortBy) ||
      "like_count".equalsIgnoreCase(sortBy)
    ) {
      sortColumn = "like_count";
    } else if (
      "saveCount".equalsIgnoreCase(sortBy) ||
      "save_count".equalsIgnoreCase(sortBy)
    ) {
      sortColumn = "save_count";
    } else if (
      "postCount".equalsIgnoreCase(sortBy) ||
      "post_count".equalsIgnoreCase(sortBy)
    ) {
      sortColumn = "post_count";
    }
    String sqlOrder = "DESC";
    // Customers don't want to search in ascending order because massive only cares about recent shit
    if (order != null && order.equalsIgnoreCase("asc")) sqlOrder = "ASC";
    // Shit end

    int safePage = Math.max(1, page);
    int safeSize = Math.max(1, size);
    int offset = (safePage - 1) * safeSize;
    params.addValue("limit", safeSize);
    params.addValue("offset", offset);

    sql.append(" ORDER BY ").append(sortColumn).append(" ").append(sqlOrder); // this slows down query
    sql.append(" LIMIT :limit OFFSET :offset");

    // String countSql = "SELECT COUNT(*) FROM posts p";
    // if (!where.isEmpty()) {
    //   countSql += " WHERE " + String.join(" AND ", where);
    // }
    // Number total = jdbc.queryForObject(countSql, params, Long.class);

    RowMapper<PostSummary> mapper = (rs, rowNum) -> {
      PostSummary dto = new PostSummary();
      dto.setId(rs.getInt("id"));
      dto.setContent(rs.getString("content"));
      java.sql.Timestamp ts = rs.getTimestamp("created_at");
      if (ts != null) dto.setCreatedAt(ts.toInstant());
      dto.setAuthorId(rs.getInt("author_id"));
      dto.setAuthorName(rs.getString("username"));
      dto.setPostCount(rs.getInt("post_count"));
      dto.setLikeCount(rs.getInt("like_count"));
      dto.setSaveCount(rs.getInt("save_count"));
      dto.setTags(rs.getString("tags"));
      dto.setUrls(rs.getString("urls"));
      return dto;
    };
    List<PostSummary> items = jdbc.query(sql.toString(), params, mapper);

    Pageable pageable = PageRequest.of(
      safePage - 1,
      safeSize,
      Sort.by(Sort.Direction.fromString(sqlOrder), sortColumn)
    );
    return new PageImpl<PostSummary>(items, pageable, 9999999);
  }

  @Transactional
  public UpdatePostResultDto UpdatePost(UpdatePostDto updatePostDto) {
    Optional<Post> postToUpdateOpt = postRepository.findById(
      updatePostDto.getPostId()
    );
    if (postToUpdateOpt.isEmpty()) return new UpdatePostResultDto(
      UpdatePostResultStatus.POST_NOT_FOUND,
      "Post not found"
    );
    Post postToUpdate = postToUpdateOpt.get();
    if (
      !postToUpdate.getAuthor().getId().equals(updatePostDto.getAuthorId())
    ) return new UpdatePostResultDto(
      UpdatePostResultStatus.AUTHOR_UNMATCHED,
      "Author unmatched"
    );
    postToUpdate.setContent(updatePostDto.getContent());
    postRepository.save(postToUpdate);
    PostHistory postHistory = new PostHistory();
    postHistory.setPost(postToUpdate);
    postHistory.setContent(postToUpdate.getContent());
    // TODO: update image urls
    postHistoryRepository.save(postHistory);
    return new UpdatePostResultDto(UpdatePostResultStatus.SUCCESS, "success");
  }

  public void createLike(CreateLikeCommand createLikeCommand) {
    Integer postId = createLikeCommand.getPostId();
    Integer userId = createLikeCommand.getUserId();
    User user = userRepository.findById(userId).orElseThrow();
    Post post = postRepository.findById(postId).orElseThrow();
    post.getLikes().add(user);
    postRepository.save(post);
  }

  public void removeLike(RemoveLikeCommand removeLikeCommand) {
    Integer postId = removeLikeCommand.getPostId();
    Integer userId = removeLikeCommand.getUserId();
    User user = userRepository.findById(userId).orElseThrow();
    Post post = postRepository.findById(postId).orElseThrow();
    post.getLikes().remove(user);
    postRepository.save(post);
  }
}
