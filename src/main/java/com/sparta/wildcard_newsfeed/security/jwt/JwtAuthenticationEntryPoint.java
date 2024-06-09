package com.sparta.wildcard_newsfeed.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.wildcard_newsfeed.domain.common.error.ErrorResponseDto;
import com.sparta.wildcard_newsfeed.security.jwt.enums.JwtPropertiesEnum;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j(topic = "인증 예외 필터")
@Component
@RequiredArgsConstructor
public class JwtAuthenticationEntryPoint implements AuthenticationEntryPoint {

    private final ObjectMapper objectMapper;

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        log.error("Jwt 인증 도중 예외 발생");
        String exception = (String) request.getAttribute("jwtException");

        JwtPropertiesEnum jwtPropertiesValue = JwtPropertiesEnum.fromJwtProperties(exception);
        String errorMessage = jwtPropertiesValue.getErrorMessage();

        int statusCode = HttpStatus.FORBIDDEN.value();

        ErrorResponseDto responseDto = ErrorResponseDto.builder()
                .message(errorMessage)
                .statusCode(statusCode)
                .build();
        String body = objectMapper.writeValueAsString(responseDto);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(statusCode);
        response.getWriter().write(body);
    }
}