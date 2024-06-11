package com.sparta.wildcard_newsfeed.domain.post.repository;

import com.sparta.wildcard_newsfeed.domain.post.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {
    List<PostMedia> findByPostId(Long postId);
}