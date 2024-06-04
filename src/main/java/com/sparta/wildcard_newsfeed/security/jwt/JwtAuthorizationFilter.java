package com.sparta.wildcard_newsfeed.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.wildcard_newsfeed.domain.common.error.ErrorResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.security.AuthenticationUserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
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
    private final ObjectMapper objectMapper;
    private final UserRepository userRepository;

    public JwtAuthorizationFilter(JwtUtil jwtUtil, AuthenticationUserService authenticationUserService,
                                  ObjectMapper objectMapper, UserRepository userRepository) {
        this.jwtUtil = jwtUtil;
        this.authenticationUserService = authenticationUserService;
        this.objectMapper = objectMapper;
        this.userRepository = userRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain filterChain) throws ServletException, IOException {

        String accessTokenValue = jwtUtil.getAccessTokenFromHeader(req);
        // TODO
        if (StringUtils.hasText(accessTokenValue) && !jwtUtil.validateToken(accessTokenValue)) {
            log.info("Access Token 검증");
            if (!jwtUtil.validateToken(accessTokenValue)) {
                log.error("Access Token이 만료되었습니다.");

                unsuccessAccessToken(res);
            } else {
                //access 유효한 경우
                Claims info = jwtUtil.getUserInfoFromToken(accessTokenValue);
                setAuthentication(info.getSubject());
            }
        }

        filterChain.doFilter(req, res);
    }

    private void unsuccessAccessToken(HttpServletResponse res) throws IOException {
        log.error("해당 Access Token이 만료되었습니다.");

        int httpStatus = HttpStatus.FORBIDDEN.value();
        ErrorResponseDto errorResponse = ErrorResponseDto.builder()
                .message("Access Token이 만료된 상태입니다.")
                .statusCode(httpStatus)
                .build();
        String body = objectMapper.writeValueAsString(errorResponse);

        res.setStatus(httpStatus);
        res.setContentType("text/html;charset=UTF-8");
        res.setContentType(MediaType.APPLICATION_JSON_VALUE);
        res.getWriter().write(body);
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