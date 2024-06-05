package com.sparta.wildcard_newsfeed.domain.user.controller;

import com.sparta.wildcard_newsfeed.domain.common.CommonResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.service.UserService;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<CommonResponseDto<UserSignupResponseDto>> signup(@Valid @RequestBody UserSignupRequestDto requestDto) {
        UserSignupResponseDto responseDto = userService.signup(requestDto);

        return ResponseEntity.ok()
                .body(CommonResponseDto.<UserSignupResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("회원가입 성공")
                        .data(responseDto)
                        .build());
    }

    @PostMapping("/resign")
    public ResponseEntity<CommonResponseDto<UserSignupResponseDto>> resign(@Valid @RequestBody UserSignupRequestDto requestDto) {
        UserSignupResponseDto responseDto = userService.resign(requestDto);

        return ResponseEntity.ok()
                .body(CommonResponseDto.<UserSignupResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("회원탈퇴 성공")
                        .data(responseDto)
                        .build());
    }

    @GetMapping("/{userId}")
    public ResponseEntity<CommonResponseDto<UserResponseDto>> getUser(@PathVariable Long userId) {
        UserResponseDto userResponseDto = userService.findById(userId);

        return ResponseEntity.ok()
                .body(CommonResponseDto.<UserResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("프로필 조회 성공")
                        .data(userResponseDto)
                        .build());
    }

    @PutMapping("/{userId}")
    public ResponseEntity<CommonResponseDto<UserResponseDto>> updateUser(
            @AuthenticationPrincipal AuthenticationUser loginUser,
            @PathVariable Long userId,
            @Valid @RequestBody UserRequestDto requestDto
    ) {
        UserResponseDto userResponseDto = userService.updateUser(loginUser, userId, requestDto);

        return ResponseEntity.ok()
                .body(CommonResponseDto.<UserResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("프로필 수정 성공")
                        .data(userResponseDto)
                        .build());
    }
}