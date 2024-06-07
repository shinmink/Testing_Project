package com.sparta.wildcard_newsfeed.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

@Getter
@NoArgsConstructor
public class UserSignupRequestDto {

    @Schema(description = "사용자 ID", example = "userid1234")
    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "대소문자 포함 영문 + 숫자만 입력해 주세요")
    @Size(min = 10, max = 20, message = "최소 10자 이상, 20자 이하로 입력해 주세요")
    @NotBlank(message = "아이디를 작성해주세요")
    private String usercode;

    @Schema(description = "사용자 이름", example = "currentPWD12@@")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&]).*$", message = "비밀번호는 대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해 주세요")
    @Size(min = 10, message = "최소 10자 이상 입력해 주세요")
    @NotBlank(message = "비밀번호를 작성해주세요")
    private String password;

    @Schema(description = "사용자 이메일", example = "test@gmail.com")
    @Email(regexp = "^[a-zA-Z0-9+-\\_.]+@[a-zA-Z0-9-]+\\.[a-zA-Z0-9-.]+$",
            message = "이메일 형식에 맞지 않습니다.")
    @NotBlank(message = "이메일의 입력 값이 없습니다.")
    @Length(message = "이메일 입력 범위를 초과하였습니다.")
    private String email;

}
