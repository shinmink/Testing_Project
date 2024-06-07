package com.sparta.wildcard_newsfeed.exception.customexception;

import lombok.Getter;

@Getter
public class AuthCodeNotFoundException extends RuntimeException {
    public AuthCodeNotFoundException() {
        super("찾을 수 없는 인증 번호입니다.");
    }
}

