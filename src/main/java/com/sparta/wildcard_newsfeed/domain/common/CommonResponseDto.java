package com.sparta.wildcard_newsfeed.domain.common;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponseDto<T> {
    private Integer statusCode;
    private String message;
    private T data;
}