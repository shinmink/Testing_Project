package com.sparta.wildcard_newsfeed.domain.comment.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CommentRequestDto {

    @NotBlank(message = "댓글 내용은 필수 입력 값입니다.")
    private String content;

    public CommentRequestDto(String content) {
        this.content = content;
    }
}