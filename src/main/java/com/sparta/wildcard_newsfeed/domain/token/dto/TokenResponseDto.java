package com.sparta.wildcard_newsfeed.domain.token.dto;

import com.sparta.wildcard_newsfeed.domain.user.dto.UserResponseFromTokenDto;
import com.sparta.wildcard_newsfeed.security.jwt.dto.TokenDto;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TokenResponseDto {
    private String usercode;
    private String accessToken;
    private String refreshToken;

    public static TokenResponseDto of(UserResponseFromTokenDto findUser, TokenDto tokenDto) {
        return TokenResponseDto.builder()
                .usercode(findUser.getUsercode())
                .accessToken(tokenDto.getAccessToken())
                .refreshToken(tokenDto.getRefreshToken())
                .build();
    }
}