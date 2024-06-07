package com.sparta.wildcard_newsfeed.domain.liked.controller;

import com.sparta.wildcard_newsfeed.domain.common.CommonResponseDto;
import com.sparta.wildcard_newsfeed.domain.liked.dto.LikedRequestDto;
import com.sparta.wildcard_newsfeed.domain.liked.dto.LikedResponseDto;
import com.sparta.wildcard_newsfeed.domain.liked.service.LikedService;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/like")
public class LikedController {

    private final LikedService likedService;

    @PostMapping
    @Operation(summary = "좋아요 추가")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 추가 성공")
    })
    public ResponseEntity<CommonResponseDto<LikedResponseDto>> addLike(
            @AuthenticationPrincipal AuthenticationUser user,
            @RequestBody LikedRequestDto requestDto
    ) {
        LikedResponseDto responseDto = likedService.addLike(requestDto, user);
        return ResponseEntity.ok()
                .body(CommonResponseDto.<LikedResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("좋아요 추가 성공")
                        .data(responseDto)
                        .build());
    }

    @DeleteMapping
    @Operation(summary = "좋아요 제거")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "좋아요 제거 성공")
    })
    public ResponseEntity<CommonResponseDto<LikedResponseDto>> removeLike(
            @AuthenticationPrincipal AuthenticationUser user,
            @RequestBody LikedRequestDto requestDto
    ) {
        likedService.removeLike(requestDto, user);
        return ResponseEntity.ok()
                .body(CommonResponseDto.<LikedResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("좋아요 제거 성공")
                        .build());
    }
}