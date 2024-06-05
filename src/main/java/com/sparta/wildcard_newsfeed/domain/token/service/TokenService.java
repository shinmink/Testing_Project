package com.sparta.wildcard_newsfeed.domain.token.service;

import com.sparta.wildcard_newsfeed.domain.token.dto.TokenResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.exception.customexception.TokenNotFoundException;
import com.sparta.wildcard_newsfeed.exception.customexception.UserNotFoundException;
import com.sparta.wildcard_newsfeed.security.jwt.JwtUtil;
import com.sparta.wildcard_newsfeed.security.jwt.dto.TokenDto;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TokenService {

    private final JwtUtil jwtUtil;
    private final UserRepository userRepository;

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


    public TokenResponseDto getFindUser(String refreshTokenHeader) {
        String usercode = jwtUtil.getUserInfoFromToken(refreshTokenHeader).getSubject();
        User findUser = userRepository.findByUsercode(usercode).orElseThrow(UserNotFoundException::new);

        TokenDto tokenDto = jwtUtil.generateAccessTokenAndRefreshToken(findUser.getUsercode());
        String refreshTokenValue = tokenDto.getRefreshToken().substring(7);
        findUser.setRefreshToken(refreshTokenValue);

        userRepository.save(findUser);

        return TokenResponseDto.of(findUser,tokenDto);

    }
}
