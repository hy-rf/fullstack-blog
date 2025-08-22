package com.backend.dto.post;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ReplyDTO {
    private Long id;
    private String content;
    private AuthorDto author;
    private LocalDateTime created;
    private LocalDateTime updatedAt;
    private List<ReplyDTO> replies;
}