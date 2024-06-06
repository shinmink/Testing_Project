package com.sparta.wildcard_newsfeed.domain.user.repository;

import com.sparta.wildcard_newsfeed.domain.user.entity.AuthCodeHistory;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AuthCodeRepository extends JpaRepository<AuthCodeHistory, Long> {
    Optional<AuthCodeHistory> findTop1ByUserAndAutoCodeOrderByExpireDateDesc(User user, String autoCode);
}
