package com.sparta.wildcard_newsfeed.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class UserEmailRequestDto {

    @NotBlank(message = "인증 번호를 입력해 주세요")
    private String authCode;
}