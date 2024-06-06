package com.sparta.wildcard_newsfeed.domain.token.controller;

import com.sparta.wildcard_newsfeed.domain.common.CommonResponseDto;
import com.sparta.wildcard_newsfeed.domain.token.dto.TokenResponseDto;
import com.sparta.wildcard_newsfeed.domain.token.service.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Token 컨트롤러", description = "Token API")
public class TokenController {

    private final TokenService tokenService;

    @PostMapping("/reissue")
    @Operation(summary = "토큰 재발급")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "토큰 재발급 성공",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = CommonResponseDto.class)))
    })
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