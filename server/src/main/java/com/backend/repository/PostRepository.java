package com.backend.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;
import org.springframework.stereotype.Repository;
import com.backend.controller.dto.post.PostSummary;
import com.backend.model.Post;
import com.backend.service.dto.post.PostWithNumbersOfRepliesDTO;


@Repository
public interface PostRepository
        extends JpaRepository<Post, Integer>, JpaSpecificationExecutor<Post> {

    Optional<List<Post>> findByAuthorId(Integer userId);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"author"})
    Optional<Post> findById(@NonNull Integer id);

    @Override
    @NonNull
    @EntityGraph(attributePaths = {"author"})
    Page<Post> findAll(@Nullable Specification<Post> spec, @NonNull Pageable pageable);


    @Query(value = """
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
                      """, nativeQuery = true)

    PostWithNumbersOfRepliesDTO find();

    @Query(value = """
                    SELECT
            p.id,
            p.content,
            p.created_at,
            u.username,
            COUNT(pc.id) AS post_count
            FROM posts p
            LEFT JOIN posts pc ON p.id = pc.root_post_id
            LEFT JOIN users u ON u.id = p.author_id
            WHERE p.root_post_id IS NULL
            GROUP BY p.id, p.content, p.created_at, u.username
            ORDER BY p.created_at DESC;
                    """, nativeQuery = true)
    List<PostSummary> findAllByRootPostIsNullOrderByCreatedAtDesc();

}
