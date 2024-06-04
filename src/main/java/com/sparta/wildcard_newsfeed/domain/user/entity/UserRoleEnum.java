package com.sparta.wildcard_newsfeed.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRoleEnum {
    USER("USER"),
    ADMIN("ADMIN"),
    ;

    private final String roleValue;
}
