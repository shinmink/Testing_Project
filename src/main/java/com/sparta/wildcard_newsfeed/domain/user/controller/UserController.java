package com.sparta.wildcard_newsfeed.domain.user.controller;

import com.sparta.wildcard_newsfeed.domain.common.CommonResponseDto;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}