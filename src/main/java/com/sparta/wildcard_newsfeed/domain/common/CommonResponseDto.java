package com.sparta.wildcard_newsfeed.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponseDto<T> {
    private Integer statusCode;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
}