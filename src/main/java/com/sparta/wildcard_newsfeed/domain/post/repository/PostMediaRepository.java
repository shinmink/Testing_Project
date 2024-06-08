package com.sparta.wildcard_newsfeed.domain.post.repository;

import com.sparta.wildcard_newsfeed.domain.post.entity.PostMedia;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostMediaRepository extends JpaRepository<PostMedia, Long> {
}