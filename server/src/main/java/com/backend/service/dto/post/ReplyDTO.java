package com.backend.service.dto.post;

import java.time.OffsetDateTime;
import java.util.List;

import lombok.Data;

@Data
public class ReplyDTO {
    private Long id;
    private String content;
    private AuthorDto author;
    private OffsetDateTime created;
    private List<ReplyDTO> replies;
}
