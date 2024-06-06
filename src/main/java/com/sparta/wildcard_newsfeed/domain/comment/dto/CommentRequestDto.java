package com.sparta.wildcard_newsfeed.domain.comment.dto;

import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용은 필수 입력 값입니다.")
    private String comment;
    private User user;

    public CommentRequestDto() {
        // 기본 생성자
    }

    public CommentRequestDto(String comment, User user) {
        this.comment = comment;
        this.user = user;
    }
}