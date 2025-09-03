package com.backend.repository;

import com.backend.controller.dto.post.PostSummary;
import com.backend.model.Post;
import com.backend.repository.dto.PostPage;
import com.backend.service.dto.post.PostWithNumbersOfRepliesDTO;
import java.util.List;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PostRepository
  extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {
  Optional<List<Post>> findByAuthorId(Integer userId);

  @Override
  @NonNull
  @EntityGraph(attributePaths = { "author" })
  Optional<Post> findById(@NonNull Integer id);

  @Override
  @NonNull
  @EntityGraph(attributePaths = { "author" })
  Page<Post> findAll(
    @Nullable Specification<Post> spec,
    @NonNull Pageable pageable
  );

  @Query(
    value = """
          SELECT
        p.id,
        p.content,
        p.created_at,
        u.id AS authorId,
        u.username AS author_name,
        STRING_AGG(r.name, ', ') AS user_role_name_list,
        COUNT(rep.id) AS number_of_replies
    FROM
        posts p
    LEFT JOIN
        users u
            ON p.author_id = u.id
    LEFT JOIN
        user_roles ur
            ON u.id = ur.user_id
    LEFT JOIN
        roles r
            ON ur.role_id = r.id
    LEFT JOIN
        reply rep
            ON rep.post_id = p.id
    WHERE
        p.id = 1
    GROUP BY
        p.id, p.content, p.created_at, p.author_id, u.id, u.username;
              """,
    nativeQuery = true
  )
  PostWithNumbersOfRepliesDTO find();

  @Query(
    value = """
            SELECT
    p.id,
    p.content,
    p.created_at,
    p.author_id,
    u.username,
    COUNT(pc.id) AS post_count,
    COUNT(DISTINCT l.user_id) AS like_count,
    COUNT(DISTINCT usp.user_id) AS save_count
    FROM posts p
    LEFT JOIN posts pc ON p.id = pc.post_id
    LEFT JOIN users u ON u.id = p.author_id
    LEFT JOIN post_likes l ON p.id = l.post_id
    LEFT JOIN user_saved_posts usp ON p.id = usp.post_id
    WHERE p.root_post_id IS NULL
    GROUP BY p.id, p.content, p.created_at, u.username, usp.post_id
    ORDER BY p.created_at DESC LIMIT :limit OFFSET :offset;
            """,
    nativeQuery = true
  )
  List<PostSummary> findAllByPostSummariesAndOffset(
    Integer offset,
    @Nullable Integer limit
  );

  @Query(
    value = """
            SELECT
    p.id,
    p.content,
    p.created_at,
    p.author_id,
    u.username,
    p.root_post_id,
    p.post_id AS parent_post_id,
    COUNT(pc.id) AS post_count,
    COUNT(DISTINCT l.user_id) AS like_count
    FROM posts p
    LEFT JOIN posts pc ON p.id = pc.post_id
    LEFT JOIN users u ON u.id = p.author_id
    LEFT JOIN post_likes l ON p.id = l.post_id
    WHERE p.post_id = :id OR p.id = :id
    GROUP BY p.id, p.content, p.created_at, u.username
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
}
