package com.sparta.wildcard_newsfeed.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

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
    @Setter
    private String refreshToken;
    @Enumerated(EnumType.STRING)
    private UserStatusEnum userStatus;
    private LocalDateTime authUserAt;

    @Enumerated(EnumType.STRING)
    private UserRoleEnum userRoleEnum;

    @Builder
    public User(String usercode, String password, UserRoleEnum userRoleEnum) {
        this.usercode = usercode;
        this.password = password;
        this.userRoleEnum = userRoleEnum;
    }
}