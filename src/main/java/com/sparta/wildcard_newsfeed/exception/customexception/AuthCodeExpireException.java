package com.sparta.wildcard_newsfeed.exception.customexception;

import lombok.Getter;

@Getter
public class AuthCodeExpireException extends RuntimeException {
    public AuthCodeExpireException() {
        super("유효하지 않은 인증 번호입니다.");
    }
}