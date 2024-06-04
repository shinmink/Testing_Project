package com.sparta.wildcard_newsfeed.domain.user.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String usercode;
    private String password;
    private String name;
    private String email;
    private String introduce;
    private String refreshToken;
    @Enumerated(EnumType.STRING)
    private UserStatusEnum userStatus;
    private LocalDateTime authUserAt;
}