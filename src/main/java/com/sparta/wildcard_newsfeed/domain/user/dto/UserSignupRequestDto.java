package com.sparta.wildcard_newsfeed.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupRequestDto {

    @Schema(description = "사용자 ID", example = "userid1234")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "대소문자 포함 영문 + 숫자만 입력해 주세요")
    @Size(min = 10, max = 20, message = "최소 10자 이상, 20자 이하로 입력해 주세요")
    private String usercode;

    @Schema(description = "사용자 이름", example = "currentPWD12@@")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).*$", message = "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해 주세요")
    @Size(min = 10, message = "최소 10자 이상 입력해 주세요")
    private String password;
}