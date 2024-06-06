package com.sparta.wildcard_newsfeed.domain.comment.service;

import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentRequestDto;
import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentResponseDto;
import com.sparta.wildcard_newsfeed.domain.comment.entity.Comment;
import com.sparta.wildcard_newsfeed.domain.comment.repository.CommentRepository;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public CommentResponseDto save(long postId, CommentRequestDto request) {
        // DB에 일정이 존재하지 않는 경우
        Post post = findPostById(postId);
        Comment comment = commentRepository.save(new Comment(request.getComment(), request.getUser(), post));
        return CommentResponseDto.toDto(commentRepository.save(comment));
    }


    public CommentResponseDto update(long postId, long commentId, CommentRequestDto request) {
        // DB에 일정이 존재하지 않는 경우
        findPostById(postId);
        // 해당 댓글이 DB에 존재하지 않는 경우
        Comment comment = findCommentById(commentId);

        // 사용자가 일치하지 않는 경우
        if (!Objects.equals(comment.getUser().getName(), request.getUser().getName())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        comment.update(request.getComment());
        return CommentResponseDto.toDto(comment);
    }

    public void delete(long postId, long commentId, String username) {

        // DB에 일정이 존재하지 않는 경우
        findPostById(postId);
        // 해당 댓글이 DB에 존재하지 않는 경우
        Comment comment = findCommentById(commentId);
        // 작성자가 동일하지 않는 경우
        if (!Objects.equals(comment.getUser().getName(), username)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);

    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
    }

    private Post findPostById(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
    }
}