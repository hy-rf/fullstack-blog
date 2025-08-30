package com.backend.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import com.backend.controller.dto.post.PostSummary;
import com.backend.model.Post;
import com.backend.service.dto.post.PostDTO;

@Component
public class PostMapper {

    public PostSummary toPostSummary(Post post) {
        PostSummary postListViewModel = new PostSummary();
        postListViewModel.setId(post.getId());
        postListViewModel.setContent(post.getContent());
        postListViewModel.setAuthor(post.getAuthor().toAuthorViewModel());
        postListViewModel.setCreatedAt(post.getCreatedAt());
        postListViewModel.setPostCount(post.getPosts().size());
        return postListViewModel;
    }

    public PostDTO toPostDTO(Post post, int depth) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setContent(post.getContent());
        postDTO.setAuthor(post.getAuthor().toAuthorDto());
        postDTO.setCreatedAt(post.getCreatedAt());
        if (depth > 0) {
            postDTO.setPosts(post.getPosts().stream().filter(p -> p.getParentPost() == null)
                    .map(reply -> toPostDTO(reply, depth)).collect(Collectors.toList()));
        }
        return postDTO;
    }
}
