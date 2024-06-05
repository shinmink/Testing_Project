package com.sparta.wildcard_newsfeed.domain.post.service;

import com.sparta.wildcard_newsfeed.domain.post.dto.PostRequestDto;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostResponseDto;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostRepository;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.entity.UserStatusEnum;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import com.sparta.wildcard_newsfeed.security.jwt.JwtUtil;
import io.jsonwebtoken.Claims;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public PostResponseDto findById(long id) {
        Post post = findPostById(id);
        return PostResponseDto.toDto(post);
    }

    public List<PostResponseDto> findAll() {
        List<Post> postlist = postRepository.findAll();
        return postlist.stream()
                .sorted(Comparator.comparing(Post::getCreatedAt).reversed())
                .map(PostResponseDto::toDto)
                .toList();
    }

    @Transactional
    public PostResponseDto addPost(PostRequestDto postRequestDto, User user) {
        Post post = new Post(postRequestDto, user);
        postRepository.save(post);
        return PostResponseDto.toDto(post);
    }

    private Post findPostById(long id) {
        return postRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시물이 존재하지 않습니다."));
    }

    private void validateUser(Post post, Long userId) {
        if (!post.getUser().getId().equals(userId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
    }

    @Transactional
    public PostResponseDto updatePost(PostRequestDto postRequestDto, Long postId, HttpServletRequest request) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        String token = jwtUtil.getAccessTokenFromHeader(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new SecurityException("유효한 토큰이 아닙니다.");
        }

        Claims claims = jwtUtil.getUserInfoFromToken(token);
        String usercode = claims.getSubject();

        User user = userRepository.findByUsercode(usercode)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new SecurityException("작성자 본인만 수정할 수 있습니다.");
        }

        post.update(postRequestDto);
        postRepository.save(post);
        return PostResponseDto.toDto(post);
    }

    @Transactional
    public PostResponseDto deletePost(Long postId, HttpServletRequest request) {
        Post post = postRepository.findPostById(postId)
                .orElseThrow(() -> new IllegalArgumentException("게시글을 찾을 수 없습니다."));

        String token = jwtUtil.getAccessTokenFromHeader(request);
        if (token == null || !jwtUtil.validateToken(token)) {
            throw new SecurityException("유효한 토큰이 아닙니다.");
        }

        Claims claims = jwtUtil.getUserInfoFromToken(token);
        String usercode = claims.getSubject();

        User user = userRepository.findByUsercode(usercode)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 사용자입니다."));

        if (!post.getUser().getId().equals(user.getId())) {
            throw new SecurityException("작성자 본인만 삭제할 수 있습니다.");
        }

        postRepository.delete(post);
        return PostResponseDto.toDto(post);
    }
}