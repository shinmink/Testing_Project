package com.sparta.wildcard_newsfeed.domain.liked.service;

import com.sparta.wildcard_newsfeed.domain.comment.entity.Comment;
import com.sparta.wildcard_newsfeed.domain.comment.repository.CommentRepository;
import com.sparta.wildcard_newsfeed.domain.liked.dto.LikedRequestDto;
import com.sparta.wildcard_newsfeed.domain.liked.dto.LikedResponseDto;
import com.sparta.wildcard_newsfeed.domain.liked.entity.ContentsTypeEnum;
import com.sparta.wildcard_newsfeed.domain.liked.entity.Liked;
import com.sparta.wildcard_newsfeed.domain.liked.repository.LikedRepository;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostRepository;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.relational.core.sql.Like;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class LikedService {
    private final LikedRepository likedRepository;
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final CommentRepository commentRepository;

    @Transactional
    public LikedResponseDto addLike(LikedRequestDto requestDto, AuthenticationUser user) {
        User currentUser = userRepository.findByUsercode(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Optional<Liked> existingLike = likedRepository.findByUserIdAndContentsIdAndContentsType(currentUser.getId(), requestDto.getContentsId(), requestDto.getContentsType());

        if (existingLike.isPresent()) {
            throw new IllegalArgumentException("이미 좋아요를 눌렀습니다.");
        }

        // 본인이 작성한 게시물이나 댓글에 좋아요를 남길 수 없습니다.
        if (requestDto.getContentsType() == ContentsTypeEnum.POST) {
            Post post = postRepository.findById(requestDto.getContentsId())
                    .orElseThrow(() -> new IllegalArgumentException("게시물을 찾을 수 없습니다."));
            if (post.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalArgumentException("본인이 작성한 게시물에는 좋아요를 남길 수 없습니다.");
            }
        } else if (requestDto.getContentsType() == ContentsTypeEnum.COMMENT) {
            Comment comment = commentRepository.findById(requestDto.getContentsId())
                    .orElseThrow(() -> new IllegalArgumentException("댓글을 찾을 수 없습니다."));
            if (comment.getUser().getId().equals(currentUser.getId())) {
                throw new IllegalArgumentException("본인이 작성한 댓글에는 좋아요를 남길 수 없습니다.");
            }
        }

        Liked liked = new Liked(currentUser, requestDto.getContentsId(), requestDto.getContentsType());
        likedRepository.save(liked);

        return new LikedResponseDto(liked);
    }

    @Transactional
    public void removeLike(LikedRequestDto requestDto, AuthenticationUser user) {
        User currentUser = userRepository.findByUsercode(user.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

        Liked existingLike = likedRepository.findByUserIdAndContentsIdAndContentsType(currentUser.getId(), requestDto.getContentsId(), requestDto.getContentsType())
                .orElseThrow(() -> new IllegalArgumentException("좋아요가 존재하지 않습니다."));

        likedRepository.delete(existingLike);


    }
}