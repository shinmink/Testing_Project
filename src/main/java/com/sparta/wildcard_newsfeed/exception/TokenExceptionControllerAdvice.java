package com.sparta.wildcard_newsfeed.exception;

import com.sparta.wildcard_newsfeed.domain.common.error.ErrorResponseDto;
import com.sparta.wildcard_newsfeed.exception.customexception.TokenNotFoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Getter
@Order(1)
@RestControllerAdvice
public class TokenExceptionControllerAdvice {

    /**
     * 토큰을 찾을 수 없는 경우
     */
    @ExceptionHandler(TokenNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> illegalArgumentException(IllegalArgumentException e) {
        log.error("{} ", e.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponseDto.builder()
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .message(e.getMessage())
                        .build());
    }

}
