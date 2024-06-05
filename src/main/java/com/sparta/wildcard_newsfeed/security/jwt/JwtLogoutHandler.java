package com.sparta.wildcard_newsfeed.security.jwt;

import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.exception.customexception.TokenNotFoundException;
import com.sparta.wildcard_newsfeed.exception.customexception.UserNotFoundException;
import io.jsonwebtoken.Claims;
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
    private final UserRepository userRepository;

    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response,
                       Authentication authentication) {
        log.info("로그아웃 시도");

        String accessTokenValue = jwtUtil.getAccessTokenFromHeader(request);
        String refreshTokenValue = jwtUtil.getRefreshTokenFromHeader(request);

        if (accessTokenValue == null && refreshTokenValue == null) {
            log.error("로그아웃 시도 중 에러 발생");
            throw new TokenNotFoundException("토큰을 찾을 수 없습니다.");
        }
        String usercode = jwtUtil.getUserInfoFromToken(refreshTokenValue).getSubject();
        User findUser = userRepository.findByUsercode(usercode).orElseThrow(UserNotFoundException::new);
        findUser.setRefreshToken(null);
        userRepository.save(findUser);

        SecurityContextHolder.clearContext();
    }
}
