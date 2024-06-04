package com.sparta.wildcard_newsfeed.domain.post.service;

import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.web.access.WebInvocationPrivilegeEvaluator;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class PostService {

    private final PostRepository postRepository;
    private final WebInvocationPrivilegeEvaluator privilegeEvaluator;

    public void 임시코드(Post post) {
        Long tempPostId = 1L;
        if (!post.getId().equals(tempPostId)) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
    }

}