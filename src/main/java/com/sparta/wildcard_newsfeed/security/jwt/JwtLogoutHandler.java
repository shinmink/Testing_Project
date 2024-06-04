package com.sparta.wildcard_newsfeed.security.jwt;

import com.sparta.wildcard_newsfeed.exception.customexception.TokenNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtLogoutHandler implements LogoutHandler {

    private final JwtUtil jwtUtil;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        log.info("로그아웃 시도");

        String accessTokenHeader = jwtUtil.getAccessTokenFromHeader(request);

        if (accessTokenHeader == null) {
            log.error("로그아웃 시도 중 에러 발생");
            throw new TokenNotFoundException("해당 토큰을 찾을 수 없습니다.");
        }

        SecurityContextHolder.clearContext();
    }
}
