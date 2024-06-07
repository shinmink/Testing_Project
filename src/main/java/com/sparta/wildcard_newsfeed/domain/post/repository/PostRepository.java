package com.sparta.wildcard_newsfeed.domain.post.repository;

import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findPostById(Long postId);

    Page<Post> findAllByCreatedAtBetween(LocalDateTime firstDate, LocalDateTime lastDate, Pageable pageable);
}