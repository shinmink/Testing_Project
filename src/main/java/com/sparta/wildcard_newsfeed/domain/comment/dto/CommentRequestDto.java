package com.sparta.wildcard_newsfeed.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용은 필수 입력 값입니다.")
    private String comment;

    public CommentRequestDto() {
        // 기본 생성자
    }

    public CommentRequestDto(String comment) {
        this.comment = comment;
    }
}