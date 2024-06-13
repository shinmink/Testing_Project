package com.sparta.wildcard_newsfeed.domain.user.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Entity

@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PUBLIC)
public class AuthCodeHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    private String autoCode;

    private LocalDateTime expireDate;

    @Builder
    public AuthCodeHistory(User user, String autoCode, LocalDateTime expireDate) {
        this.user = user;
        this.autoCode = autoCode;
        this.expireDate = expireDate;
    }
}