package com.sparta.wildcard_newsfeed.domain.token.service;

import com.sparta.wildcard_newsfeed.domain.token.dto.TokenResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserResponseFromTokenDto;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.domain.user.service.UserService;
import com.sparta.wildcard_newsfeed.exception.customexception.TokenNotFoundException;
import com.sparta.wildcard_newsfeed.exception.customexception.UserNotFoundException;
import com.sparta.wildcard_newsfeed.security.jwt.JwtUtil;
import com.sparta.wildcard_newsfeed.security.jwt.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;
    private final UserService userService;

    public String validateTokenExpire(HttpServletRequest request) {
        String accessTokenHeader = jwtUtil.getAccessTokenFromHeader(request);
        String refreshTokenHeader = jwtUtil.getRefreshTokenFromHeader(request);

        //access 또는 refresh가 없는 경우
        if (accessTokenHeader == null || refreshTokenHeader == null) {
            throw new TokenNotFoundException("토큰을 찾을 수 없습니다.");
        }
        //refresh token이 유효하지 않은 경우
        if (!jwtUtil.validateToken(request, refreshTokenHeader)) {
            throw new TokenNotFoundException("유효하지 않은 토큰입니다.");
        }
        return refreshTokenHeader;
    }

    @Transactional
    public TokenResponseDto getFindUser(String refreshTokenHeader) {
        String usercode = jwtUtil.getUserInfoFromToken(refreshTokenHeader).getSubject();
        UserResponseFromTokenDto findUserDto = userService.findByUsercode(usercode);

        TokenDto tokenDto = jwtUtil.generateAccessTokenAndRefreshToken(findUserDto.getUsercode());
        String refreshTokenValue = tokenDto.getRefreshToken().substring(7);
        userService.updateRefreshToken(findUserDto.getUsercode(), refreshTokenValue);

        return TokenResponseDto.of(findUserDto, tokenDto);

    }
}
