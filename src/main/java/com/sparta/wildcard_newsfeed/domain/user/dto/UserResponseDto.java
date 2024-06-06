package com.sparta.wildcard_newsfeed.domain.user.dto;

import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    @Schema(description = "사용자 ID")
    private String usercode;
    @Schema(description = "사용자 이름")
    private String name;
    @Schema(description = "한 줄 소개")
    private String introduce;
    @Schema(description = "사용자 Email")
    private String email;

    public UserResponseDto(User user) {
        this.usercode = user.getUsercode();
        this.name = user.getName();
        this.introduce = user.getIntroduce();
        this.email = user.getEmail();
    }
}