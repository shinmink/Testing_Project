package com.sparta.wildcard_newsfeed.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@RequiredArgsConstructor
public class UserEmailRequestDto {

    @NotBlank(message = "인증 번호를 입력해 주세요")
    @Schema(description = "이메일 인증번호", example = "")
    private String authCode;
}