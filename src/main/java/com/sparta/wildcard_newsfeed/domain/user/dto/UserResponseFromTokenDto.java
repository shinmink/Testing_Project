package com.sparta.wildcard_newsfeed.domain.user.dto;

import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponseFromTokenDto {
    private String usercode;

    public static UserResponseFromTokenDto of(User user) {
        return UserResponseFromTokenDto.builder()
                .usercode(user.getUsercode())
                .build();
    }
}