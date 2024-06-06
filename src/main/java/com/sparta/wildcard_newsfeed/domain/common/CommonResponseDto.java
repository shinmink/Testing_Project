package com.sparta.wildcard_newsfeed.domain.common;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponseDto<T> {

    @Schema(description = "상태코드", example = "200")
    private Integer statusCode;

    @Schema(description = "메세지", example = "성공")
    private String message;

    @Schema(description = "응답 데이터")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;
}