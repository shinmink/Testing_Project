package com.sparta.wildcard_newsfeed.exception;

import com.sparta.wildcard_newsfeed.domain.common.error.ErrorResponseDto;
import com.sparta.wildcard_newsfeed.exception.customexception.AuthCodeExpireException;
import com.sparta.wildcard_newsfeed.exception.customexception.AuthCodeNoMatchException;
import com.sparta.wildcard_newsfeed.exception.customexception.AuthCodeNotFoundException;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@Getter
@Order(2)
@RestControllerAdvice
public class EmailExceptionControllerAdvice {

    /**
     * 유효하지 않은 인증 번호
     */
    @ExceptionHandler(AuthCodeExpireException.class)
    public ResponseEntity<ErrorResponseDto> authCodeExpireException(AuthCodeExpireException e) {
        log.info("AuthCodeExpireException {}", e.getClass().getSimpleName());
        log.error("{} ", e.getMessage());

        return ResponseEntity.status(HttpStatus.FORBIDDEN)
                .body(ErrorResponseDto.builder()
                        .statusCode(HttpStatus.FORBIDDEN.value())
                        .message(e.getMessage())
                        .build());
    }

    /**
     * 일치하지 않는 인증 번호
     */
    @ExceptionHandler(AuthCodeNoMatchException.class)
    public ResponseEntity<ErrorResponseDto> authCodeNoMatchException(AuthCodeNoMatchException e) {
        log.info("AuthCodeNoMatchException {}", e.getClass().getSimpleName());
        log.error("{} ", e.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build());
    }

    /**
     * 찾을 수 없는 인증 번호
     */
    @ExceptionHandler(AuthCodeNotFoundException.class)
    public ResponseEntity<ErrorResponseDto> authCodeNotFoundException(AuthCodeNotFoundException e) {
        log.info("AuthCodeNotFoundException {}", e.getClass().getSimpleName());
        log.error("{} ", e.getClass().getSimpleName());

        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build());
    }
}