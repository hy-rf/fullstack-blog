package com.backend.dto.post;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.Data;

@Data
public class PostDTO {
    private Long id;
    private String title;
    private String content;
    private AuthorDto author;
    private OffsetDateTime createdAt;
    private OffsetDateTime updatedAt;
    private List<ReplyDTO> replies;
}