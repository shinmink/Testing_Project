package com.sparta.wildcard_newsfeed.exception.customexception;

import lombok.Getter;

@Getter
public class TokenNotFoundException  extends RuntimeException{
    public TokenNotFoundException(String message) {
        super(message);
    }
}
