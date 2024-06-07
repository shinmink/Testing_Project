package com.sparta.wildcard_newsfeed.domain.post.dto;

import java.time.LocalDateTime;

// JPA -> DTO로 매핑할 때 호환이 잘 안되서 인터페이스로 받아야 한다고 한다
public interface PostPageResponseDto {
     Long getUser_id();
     String getTitle();
     String getContent();
     String getUsername();
     LocalDateTime getCreated_at();
     LocalDateTime getUpdated_at();
     Long getLikecount();
}
/*
private Long id;
    private Long user_id;
    private String title;
    private String content;
    private String username;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Long likecount;
 */