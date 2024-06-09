package com.sparta.wildcard_newsfeed.exception.customexception;

import lombok.Getter;

@Getter
public class AuthCodeNoMatchException extends RuntimeException {
    public AuthCodeNoMatchException() {
        super("일치하지 않는 인증 번호입니다.");
    }
}