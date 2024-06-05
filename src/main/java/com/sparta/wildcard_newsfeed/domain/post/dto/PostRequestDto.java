package com.sparta.wildcard_newsfeed.domain.post.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter

public class PostRequestDto {

    @NotBlank(message = "제목은 필수 입력 값입니다.")
    private String title;

    @NotBlank(message = "내용은 필수 입력 값입니다.")
    private String content;

    @NotBlank(message = "작성자는 필수 입력 값입니다.")
    private String username;

    // 작성일자는 보통 서버에서 자동으로 설정하지만, 필요 시 클라이언트에서 받을 수도.

    // 기본 생성자
    public PostRequestDto() {
    }

    // 모든 필드를 포함한 생성자
    public PostRequestDto(String title, String content, String username) {
        this.title = title;
        this.content = content;
        this.username = username;
    }

}