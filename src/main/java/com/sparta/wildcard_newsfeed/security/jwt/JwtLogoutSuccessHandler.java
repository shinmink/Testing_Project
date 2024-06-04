package com.sparta.wildcard_newsfeed.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.wildcard_newsfeed.domain.common.CommonResponseDto;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Slf4j
@RequiredArgsConstructor
public class JwtLogoutSuccessHandler implements LogoutSuccessHandler {

    private final ObjectMapper objectMapper;

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response,
                                Authentication authentication) throws IOException {
        log.info("로그아웃 성공");
        CommonResponseDto responseDto = CommonResponseDto.builder()
                .message("로그아웃 성공하였습니다.")
                .statusCode(HttpStatus.OK.value())
                .build();

        String body = objectMapper.writeValueAsString(responseDto);
        response.setContentType("text/html;charset=UTF-8");
        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.getWriter().write(body);
    }

}
