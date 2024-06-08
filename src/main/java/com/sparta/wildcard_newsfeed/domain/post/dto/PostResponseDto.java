package com.sparta.wildcard_newsfeed.domain.post.dto;

import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private List<String> s3Urls;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public PostResponseDto(Post post) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getName();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
    }

    public PostResponseDto(Post post, List<String> s3Urls) {
        this.id = post.getId();
        this.title = post.getTitle();
        this.content = post.getContent();
        this.username = post.getUser().getName();
        this.createdAt = post.getCreatedAt();
        this.updatedAt = post.getUpdatedAt();
        this.s3Urls = s3Urls;
    }
}