package com.sparta.wildcard_newsfeed.domain.post.dto;

import java.time.LocalDateTime;

// JPA -> DTO로 매핑할 때 호환이 잘 안되서 인터페이스로 받아야 한다고 한다
public interface PostPageResponseDto {
     Long getPostId();
     Long getUserId();
     String getTitle();
     String getContent();
     String getName();
     LocalDateTime getCreatedAt();
     LocalDateTime getUpdatedAt();
     Long getLikeCount();
}