package com.sparta.wildcard_newsfeed.domain.common.error;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class ErrorResponseDto {
    private Integer statusCode;
    private Object message;
}