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
    private Long postId;

    public CommentResponseDto(Long id, String comment, String username, Long postId, LocalDateTime createdAt) {
        this.id = id;
        this.comment = comment;
        this.username = username;
        this.postId = postId;
        this.createdAt = createdAt;
    }

    public static CommentResponseDto toDto(Comment comment) {
        return new CommentResponseDto(
                comment.getId(),
                comment.getContent(),
                comment.getUser().getName(),
                comment.getPost().getId(),
                comment.getCreatedAt()
        );
    }
}