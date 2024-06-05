package com.sparta.wildcard_newsfeed.exception;

import com.sparta.wildcard_newsfeed.domain.common.error.ErrorResponseDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class ExceptionControllerAdvice {

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ErrorResponseDto> illegalArgumentException(IllegalArgumentException e) {
        log.error("예시", e);
        return ResponseEntity.status(HttpStatus.NOT_FOUND)
                .body(ErrorResponseDto.builder()
                        .statusCode(HttpStatus.NOT_FOUND.value())
                        .message(e.getMessage())
                        .build());
    }

//    @ExceptionHandler(TokenNotFoundException.class)
//    public ResponseEntity<ErrorResponseDto> tokenNotFoundException(TokenNotFoundException e) {
//        log.error("Token 예외 발생 {} " ,e);
//        return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                .body(ErrorResponseDto.builder()
//                        .statusCode(HttpStatus.NOT_FOUND.value())
//                        .message(e.getMessage())
//                        .build()
//                );
//    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, Object>> methodArgumentNotValidException(MethodArgumentNotValidException ex) {
        Map<String, Object> responseValid = new LinkedHashMap<>();
        List<String> errorMessageList = new ArrayList<>();
        responseValid.put("statusCode", HttpStatus.BAD_REQUEST.toString());
        ex.getBindingResult().getAllErrors().forEach(v -> errorMessageList.add(v.getDefaultMessage()));
        responseValid.put("message", errorMessageList);
        return ResponseEntity.badRequest().body(responseValid);
    }
}