package com.backend.mapper;

import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.backend.dto.post.PostDTO;
import com.backend.dto.post.ReplyDTO;
import com.backend.model.Post;
import com.backend.model.Reply;

@Component
public class PostMapper {

    public PostDTO toPostDTO(Post post, int depth) {
        PostDTO postDTO = new PostDTO();
        postDTO.setId(post.getId());
        postDTO.setTitle(post.getTitle());
        postDTO.setContent(post.getContent());
        postDTO.setAuthor(post.getAuthor().toAuthorDto());
        postDTO.setCreatedAt(post.getCreatedAt());
        postDTO.setUpdatedAt(post.getUpdatedAt());
        if (depth > 0) {
            postDTO.setReplies(post.getReplies().stream()
                    .map(reply -> toReplyDTO(reply, depth - 1))
                    .collect(Collectors.toList()));
        }
        return postDTO;
    }

    public ReplyDTO toReplyDTO(Reply reply, int depth) {
        ReplyDTO replyDTO = new ReplyDTO();
        replyDTO.setId(reply.getId());
        replyDTO.setContent(reply.getContent());
        replyDTO.setCreated(reply.getCreated());
        replyDTO.setUpdatedAt(reply.getUpdatedAt());
        if (depth > 0 && reply.getReplies() != null) {
            replyDTO.setReplies(reply.getReplies().stream()
                    .map(r -> toReplyDTO(r, depth - 1))
                    .collect(Collectors.toList()));
        }
        return replyDTO;
    }
}