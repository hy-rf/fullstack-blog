package com.backend.repository;

import com.backend.controller.dto.post.PostSummary;
import com.backend.model.Post;
import com.backend.repository.dto.PostPage;
import com.backend.repository.dto.SavedPost;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository extends JpaRepository<Post, Integer> {
  @Override
  @NonNull
  @EntityGraph(attributePaths = { "author" })
  Optional<Post> findById(@NonNull Integer id);

  /**
   * Get feed posts on home page
   * Field of data type MUST be same order as in sql
   * @param offset
   * @param limit
   * @return
   */
  @Query(
    value = """
    SELECT
      p.id,
      p.content,
      p.created_at,
      p.author_id,
      u.username,
      p.post_count,
      p.like_count,
      p.save_count,
      (SELECT STRING_AGG(t.name, ', ') FROM post_tags pt JOIN tags t ON pt.tag_id = t.id WHERE pt.post_id = p.id) AS tags,
      (SELECT STRING_AGG(pi.url, ',') FROM post_images pi WHERE pi.post_id = p.id) AS urls
    FROM posts p
    JOIN users u ON u.id = p.author_id
    WHERE p.root_post_id IS NULL
    ORDER BY p.created_at DESC
    LIMIT :limit OFFSET :offset
    """,
    nativeQuery = true
  )
  List<PostSummary> findAllByPostSummariesAndOffset(
    Integer offset,
    @Nullable Integer limit
  );

  /**
   * Get post and its child posts by id
   * @param id
   * @return
   */
  @Query(
    value = """
            SELECT
    p.id,
    p.content,
    p.created_at,
    p.author_id,
    u.username,
    p.root_post_id,
    p.parent_post_id,
    p.post_count,
    p.like_count,
    p.save_count,
    (SELECT STRING_AGG(t.name, ', ') FROM post_tags pt JOIN tags t ON pt.tag_id = t.id WHERE pt.post_id = p.id) AS tags,
    (SELECT STRING_AGG(pi.url, ',') FROM post_images pi WHERE pi.post_id = p.id) AS urls
    FROM posts p
    LEFT JOIN users u ON u.id = p.author_id
    WHERE p.parent_post_id = :id OR p.id = :id
    ORDER BY p.id ASC, p.created_at DESC;
            """,
    nativeQuery = true
  )
  List<PostPage> findAllByRootPostIdOrderByCreatedAtDesc(Integer id);

  @Modifying
  @Transactional
  @Query(
    value = """
    INSERT INTO post_likes (post_id, user_id) VALUES (:postId, :userId);
    """,
    nativeQuery = true
  )
  int addLike(Integer postId, Integer userId);

  @Modifying
  @Transactional
  @Query(
    value = """
    DELETE FROM post_likes WHERE post_id = :postId AND user_id = :userId;
    """,
    nativeQuery = true
  )
  int removeLike(Integer postId, Integer userId);

  @Modifying
  @Transactional
  @Query(
    value = """
    INSERT INTO user_saved_posts (user_id, post_id) VALUES (:userId, :postId);
    """,
    nativeQuery = true
  )
  int addSavedPost(Integer userId, Integer postId);

  @Modifying
  @Transactional
  @Query(
    value = """
    DELETE FROM user_saved_posts WHERE user_id = :userId AND post_id = :postId;
    """,
    nativeQuery = true
  )
  int removeSavedPost(Integer userId, Integer postId);

  @Query(
    value = """
            SELECT
    p.id,
    p.content,
    p.created_at,
    p.author_id,
    u.username,
    p.post_count,
    p.like_count,
    p.save_count,
    (SELECT STRING_AGG(t.name, ', ') FROM post_tags pt JOIN tags t ON pt.tag_id = t.id WHERE pt.post_id = p.id) AS tags
    FROM posts p
    LEFT JOIN users u ON u.id = p.author_id
    LEFT JOIN user_saved_posts usp ON p.id = usp.post_id
    WHERE usp.user_id = :userId
    GROUP BY p.id, p.content, p.created_at, u.username, usp.post_id
    ORDER BY p.created_at DESC;
            """,
    nativeQuery = true
  )
  List<PostSummary> getSavedPostsByUserId(Integer userId);

  @Query(
    value = """
    SELECT post_id, user_id
    FROM user_saved_posts
    WHERE user_id = :userId
    """,
    nativeQuery = true
  )
  List<SavedPost> findSavedPostIdsByUserId(Integer userId);

  @Query(
    value = """
    SELECT post_id, user_id
    FROM post_likes
    WHERE user_id = :userId
    """,
    nativeQuery = true
  )
  List<SavedPost> findLikedPostIdsByUserId(Integer userId);
}
