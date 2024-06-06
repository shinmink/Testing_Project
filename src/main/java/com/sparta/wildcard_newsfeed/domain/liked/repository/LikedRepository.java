package com.sparta.wildcard_newsfeed.domain.liked.repository;

import com.sparta.wildcard_newsfeed.domain.liked.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikedRepository extends JpaRepository<Liked, Long> {
}