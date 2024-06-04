package com.sparta.wildcard_newsfeed.domain.user.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserSignupRequestDto {

    @Pattern(regexp = "^[a-zA-Z0-9]+$", message = "대소문자 포함 영문 + 숫자만 입력해 주세요")
    @Size(min = 10, max = 20, message = "최소 10자 이상, 20자 이하로 입력해 주세요")
    private String usercode;

    //(최소 8글자, 글자 1개, 숫자 1개, 특수문자 1개)
    //정규식 = ^(?=.*[A-Za-z])(?=.*\d)(?=.*[@$!%*#?&])[A-Za-z\d@$!%*#?&]{8,}$

    //(최소 8글자, 대문자 1개, 소문자 1개, 숫자 1개)
    //정규식 = ^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)[a-zA-Z\d]{8,}$
    //@Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]$", message = "대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해 주세요")
    @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)(?=.*[@$!%*#?&])[A-Za-z\\d@$!%*#?&]", message = "대소문자 포함 영문 + 숫자 + 특수문자를 최소 1글자씩 포함해 주세요")
    @Size(min = 10, message = "최소 10자 이상 입력해 주세요")
    private String password;
}
