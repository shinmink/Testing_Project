package com.sparta.wildcard_newsfeed.domain.comment.controller;

import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentRequestDto;
import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentResponseDto;
import com.sparta.wildcard_newsfeed.domain.comment.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/comment")
public class CommentController {

    private final CommentService commentService;

    // 댓글 추가
    @PostMapping
    public ResponseEntity<CommentResponseDto> addCommnt(
            @PathVariable(name = "post_id") long postId,
            @RequestBody CommentRequestDto request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(commentService.save(postId, request));
    }

    @PatchMapping("{commentId}")
    public ResponseEntity<CommentResponseDto> update(
            @PathVariable(name = "post_id") long postId,
            @PathVariable(name = "comment_id") long commentId,
            @RequestBody CommentRequestDto request) {

        return ResponseEntity.ok().body(commentService.update(postId, commentId, request));
    }

    @DeleteMapping("{commentId}")
    public ResponseEntity<String> delete(
            @PathVariable(name = "post_id") long postId,
            @PathVariable(name = "comment_id") long commentId,
            @RequestBody String username) {

        commentService.delete(postId, commentId, username);
        return ResponseEntity.ok().body("성공적으로 댓글 삭제되었습니다.");
    }
}