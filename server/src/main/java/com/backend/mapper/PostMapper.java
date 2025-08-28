package com.backend.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.backend.dto.post.PostDTO;
import com.backend.dto.post.PostListViewModel;
import com.backend.model.Post;

@Component
public class PostMapper {

    public PostListViewModel toPostListViewModel(Post post) {
        PostListViewModel postListViewModel = new PostListViewModel();
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
                    .map(reply -> toPostDTO(reply, depth - 1)).collect(Collectors.toList()));
        }
        return postDTO;
    }
}
