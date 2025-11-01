package com.backend.dao;

import com.backend.controller.dto.PostSummary;
import com.backend.dao.dto.PostPage;
import com.backend.dao.model.Post;
import io.micrometer.common.lang.Nullable;
import java.util.List;
import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends CrudRepository<Post, Integer> {
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
    """
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
            """
  )
  List<PostPage> findAllByRootPostIdOrderByCreatedAtDesc(Integer id);
}
