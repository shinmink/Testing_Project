package com.sparta.wildcard_newsfeed.domain.token.controller;

import com.sparta.wildcard_newsfeed.domain.common.CommonResponseDto;
import com.sparta.wildcard_newsfeed.domain.token.dto.TokenResponseDto;
import com.sparta.wildcard_newsfeed.domain.token.service.TokenService;
import com.sparta.wildcard_newsfeed.security.jwt.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.sparta.wildcard_newsfeed.security.jwt.JwtConstants.ACCESS_TOKEN_HEADER;
import static com.sparta.wildcard_newsfeed.security.jwt.JwtConstants.REFRESH_TOKEN_HEADER;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/reissue")
    public ResponseEntity<CommonResponseDto> refreshTokenReissue(HttpServletRequest request) {
        log.info("access token 재발급");
        String refreshTokenHeader = tokenService.validateTokenExpire(request);

        TokenResponseDto responseDto = tokenService.getFindUser(refreshTokenHeader);

        CommonResponseDto commonResponseDto = CommonResponseDto.builder()
                .message(responseDto.getUsercode() + "님 재발급 성공")
                .statusCode(HttpStatus.OK.value())
                .build();

        return ResponseEntity.ok()
                .header(ACCESS_TOKEN_HEADER, responseDto.getAccessToken())
                .header(REFRESH_TOKEN_HEADER, responseDto.getRefreshToken())
                .body(commonResponseDto);

    }


}