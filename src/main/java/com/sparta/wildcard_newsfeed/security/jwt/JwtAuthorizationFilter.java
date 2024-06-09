package com.sparta.wildcard_newsfeed.security.jwt;

import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.exception.customexception.TokenNotFoundException;
import com.sparta.wildcard_newsfeed.exception.customexception.UserNotFoundException;
import com.sparta.wildcard_newsfeed.security.AuthenticationUserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j(topic = "JWT 검증 및 인가")
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AuthenticationUserService authenticationUserService;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, AuthenticationUserService authenticationUserService, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationUserService = authenticationUserService;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {
        String accessTokenValue = jwtUtil.getAccessTokenFromHeader(req);

        log.info("access token 검증");
        if (StringUtils.hasText(accessTokenValue) && jwtUtil.validateToken(req, accessTokenValue)) {
            log.info("refresh token 검증");

            String refreshTokenValue = jwtUtil.getRefreshTokenFromHeader(req);
            if (StringUtils.hasText(refreshTokenValue) && jwtUtil.validateToken(req, refreshTokenValue)) {
                String usercode = jwtUtil.getUserInfoFromToken(refreshTokenValue).getSubject();
                User findUser = userRepository.findByUsercode(usercode)
                        .orElseThrow(UserNotFoundException::new);

                if (isValidateUserAndToken(usercode, findUser, refreshTokenValue)) {
                    //access token 및 refresh token 검증 완료
                    log.info("Token 인증 완료");
                    Claims info = jwtUtil.getUserInfoFromToken(accessTokenValue);
                    setAuthentication(info.getSubject());
                }
            } else {
                log.error("유효하지 않는 Refersh Token");
                throw new TokenNotFoundException("토큰에 문제가 생김");
            }
        }
        filterChain.doFilter(req, res);
    }

    private boolean isValidateUserAndToken(String usercode, User findUser, String refreshTokenValue) {
        if (usercode.equals(findUser.getUsercode())
                && refreshTokenValue.equals(findUser.getRefreshToken())) {
            return true;
        }
        return false;
    }


    // 인증 처리
    public void setAuthentication(String username) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        Authentication authentication = createAuthentication(username);
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
    }

    // 인증 객체 생성
    private Authentication createAuthentication(String username) {
        UserDetails userDetails = authenticationUserService.loadUserByUsername(username);
        return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
    }
}