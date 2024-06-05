package com.sparta.wildcard_newsfeed.domain.user.dto;

import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UserResponseDto {

    private String usercode;
    private String name;
    private String introduce;
    private String email;

    public UserResponseDto(User user) {
        this.usercode = user.getUsercode();
        this.name = user.getName();
        this.introduce = user.getIntroduce();
        this.email = user.getEmail();
    }
}