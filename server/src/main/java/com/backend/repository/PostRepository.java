package com.backend.repository;

import java.time.OffsetDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import com.backend.dto.post.PostDTO;
import com.backend.dto.post.PostWithNumbersOfRepliesDTO;
import com.backend.model.Post;


@Repository
public interface PostRepository extends JpaRepository<Post, Long>, JpaSpecificationExecutor<Post> {

  Optional<List<Post>> findByAuthorId(Long userId);

  @Override
  @NonNull
  @EntityGraph(attributePaths = {"author", "replies", "replies"})
  Optional<Post> findById(@NonNull Long id);

  @Override
  @NonNull
  @EntityGraph(attributePaths = {"author", "replies"})
  Page<Post> findAll(@Nullable Specification<Post> spec, @NonNull Pageable pageable);


  @Query(value = """
            SELECT
          p.id,
          p.title,
          p.content,
          p.created_at,
          p.updated_at,
          u.id AS authorId,
          u.username,
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
          p.id, p.title, p.content, p.created_at, p.updated_at, p.author_id, u.id, u.username;
                """, nativeQuery = true)

  PostWithNumbersOfRepliesDTO find();

}
