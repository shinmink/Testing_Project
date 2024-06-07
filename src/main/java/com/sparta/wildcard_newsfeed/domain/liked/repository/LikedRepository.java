package com.sparta.wildcard_newsfeed.domain.liked.repository;

import com.sparta.wildcard_newsfeed.domain.liked.entity.ContentsTypeEnum;
import com.sparta.wildcard_newsfeed.domain.liked.entity.Liked;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.relational.core.sql.Like;

import java.util.Optional;

public interface LikedRepository extends JpaRepository<Liked, Long> {
    Optional<Liked> findByUserIdAndContentsIdAndContentsType(Long user_id, Long contentsId, ContentsTypeEnum contentsType);

    void deleteByUserIdAndContentsIdAndContentsType(Long user_id, Long contentsId, ContentsTypeEnum contentsType);
}
