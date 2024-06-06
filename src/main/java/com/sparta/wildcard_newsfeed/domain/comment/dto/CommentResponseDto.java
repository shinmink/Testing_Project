package com.sparta.wildcard_newsfeed.domain.comment.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.sparta.wildcard_newsfeed.domain.comment.entity.Comment;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class CommentResponseDto {
    private Long id;
    private String comment;
    private String username;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long postId;

    public CommentResponseDto(Comment comment) {
        this.id = comment.getId();
        this.comment = comment.getContent();
        this.username = comment.getUser().getName();
        this.postId = comment.getPost().getId();
        this.createdAt = comment.getCreatedAt();
        this.updatedAt = comment.getUpdatedAt();
    }
}