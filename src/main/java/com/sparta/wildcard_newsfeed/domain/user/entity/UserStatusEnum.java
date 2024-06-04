package com.sparta.wildcard_newsfeed.domain.user.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserStatusEnum {
    ENABLED("enable"), DISABLED("disable");
    private final String status;
}