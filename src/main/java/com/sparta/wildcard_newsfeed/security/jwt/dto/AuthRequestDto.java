package com.sparta.wildcard_newsfeed.security.jwt.dto;

import lombok.Getter;

@Getter
public class AuthRequestDto {
    private String usercode;
    private String password;
}