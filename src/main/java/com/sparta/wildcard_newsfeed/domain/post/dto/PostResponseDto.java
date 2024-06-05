package com.sparta.wildcard_newsfeed.domain.post.dto;

import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import lombok.Getter;

@Getter
public class PostResponseDto {
    private Long id;
    private String title;
    private String content;
    private String username;
    private String date;

    public PostResponseDto(Long id, String title, String content, String username, String date) {
        this.id = id;
        this.title = title;
        this.content = content;
        this.username = username;
        this.date = date;
    }

    public PostResponseDto() {
    }

    public static PostResponseDto toDto(Post post) {
        return new PostResponseDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getUser().getName(),
                post.getCreatedAt().toString() // 또는 적절한 형식으로 날짜를 포맷팅
        );
    }
}