package com.sparta.wildcard_newsfeed.domain.comment.controller;

import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentRequestDto;
import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentResponseDto;
import com.sparta.wildcard_newsfeed.domain.comment.service.CommentService;
import com.sparta.wildcard_newsfeed.domain.common.CommonResponseDto;
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
@RequestMapping("/api/v1/post/{postId}/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 추가
    @PostMapping
    public ResponseEntity<CommonResponseDto<CommentResponseDto>> addComment(
            @AuthenticationPrincipal AuthenticationUser user,
            @PathVariable(name = "postId") long postId,
            @RequestBody CommentRequestDto commentRequestDto
    ) {
        CommentResponseDto commentResponseDto = commentService.addComment(postId, commentRequestDto, user);
        return ResponseEntity.ok()
                .body(CommonResponseDto.<CommentResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("댓글 등록 성공")
                        .data(commentResponseDto)
                        .build());
    }

    @PutMapping("{commentId}")
    public ResponseEntity<CommonResponseDto<CommentResponseDto>> updateComment(
            @AuthenticationPrincipal AuthenticationUser user,
            @Valid @RequestBody CommentRequestDto commentRequestDto,
            @PathVariable(name = "postId") Long postId,
            @PathVariable(name = "commentId") long commentId
    ) {
        CommentResponseDto commentResponseDto = commentService.updateComment(postId, commentId, commentRequestDto, user);
        return ResponseEntity.ok()
                .body(CommonResponseDto.<CommentResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("댓글 수정 성공")
                        .data(commentResponseDto)
                        .build());
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<CommonResponseDto<CommentResponseDto>> deleteComment(
            @AuthenticationPrincipal AuthenticationUser user,
            @PathVariable(name = "postId") long postId,
            @PathVariable(name = "commentId") long commentId
    ) {
        commentService.deleteComment(postId, commentId, user.getUsername());
        return ResponseEntity.ok()
                .body(CommonResponseDto.<CommentResponseDto>builder()
                        .statusCode(HttpStatus.OK.value())
                        .message("댓글 삭제 성공")
                        .build());
    }
}