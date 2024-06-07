package com.sparta.wildcard_newsfeed.domain.user.repository;

import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsercode(String usercode);

    Optional<User> findByUsercodeOrEmail(String usercode, String email);
}