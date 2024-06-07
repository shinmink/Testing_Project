package com.sparta.wildcard_newsfeed.domain.comment.service;

import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentRequestDto;
import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentResponseDto;
import com.sparta.wildcard_newsfeed.domain.comment.entity.Comment;
import com.sparta.wildcard_newsfeed.domain.comment.repository.CommentRepository;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostRepository;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CommentService {

    private final PostRepository postRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    @Transactional
    public CommentResponseDto addComment(long postId, CommentRequestDto request, AuthenticationUser user) {
        User byUsercode = userRepository.findByUsercode(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        // DB에 게시물이 존재하지 않는 경우
        Post post = findPostById(postId);
        Comment comment = commentRepository.save(new Comment(request.getComment(), byUsercode, post));
        return new CommentResponseDto(comment);
    }

    public CommentResponseDto updateComment(long postId, long commentId, CommentRequestDto request, AuthenticationUser user) {
        // DB에 게시물이 존재하지 않는 경우
        findPostById(postId);
        // 해당 댓글이 DB에 존재하지 않는 경우
        Comment comment = findCommentById(commentId);

        // 작성자가 동일하지 않는 경우
        if (!Objects.equals(comment.getUser().getUsercode(), user.getUsername())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }

        comment.update(request.getComment());
        commentRepository.save(comment);
        return new CommentResponseDto(comment);
    }

    public void deleteComment(long postId, long commentId, String username) {
        // DB에 게시물이 존재하지 않는 경우
        findPostById(postId);

        // 해당 댓글이 DB에 존재하지 않는 경우
        Comment comment = findCommentById(commentId);

        // 작성자가 동일하지 않는 경우
        if (!Objects.equals(comment.getUser().getUsercode(), username)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }

        commentRepository.delete(comment);
    }

    public List<CommentResponseDto> findAllCommentsByPostId(long postId) {
        // 해당 postId와 연관된 댓글을 조회하는 로직 구현
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(CommentResponseDto::new)
                .collect(Collectors.toList());
    }

    public Comment findCommentById(Long commentId) {
        return commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
    }

    private Post findPostById(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
    }

    public List<CommentResponseDto> findAll() {
        List<Comment> commentlist = commentRepository.findAll();
        return commentlist.stream()
                .sorted(Comparator.comparing(Comment::getCreatedAt).reversed())
                .map(CommentResponseDto::new)
                .toList();
    }
}