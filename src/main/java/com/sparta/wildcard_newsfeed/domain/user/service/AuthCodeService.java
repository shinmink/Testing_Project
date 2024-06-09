package com.sparta.wildcard_newsfeed.domain.user.service;

import com.sparta.wildcard_newsfeed.domain.user.entity.AuthCodeHistory;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.AuthCodeRepository;
import com.sparta.wildcard_newsfeed.exception.customexception.AuthCodeExpireException;
import com.sparta.wildcard_newsfeed.exception.customexception.AuthCodeNoMatchException;
import com.sparta.wildcard_newsfeed.exception.customexception.AuthCodeNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.UUID;

@Slf4j
@Primary
@Service
@RequiredArgsConstructor
public class AuthCodeService {

    private final AuthCodeRepository authCodeRepository;
    private static final Long MAX_EXPIRE_TIME = 180L;

    /**
     * 등록
     */
    @Transactional
    public AuthCodeHistory addAuthCode(User user) {
        AuthCodeHistory authCodeHistory = AuthCodeHistory.builder()
                .user(user)
                .autoCode(createAuthCode())
                .expireDate(LocalDateTime.now().plusSeconds(MAX_EXPIRE_TIME))
                .build();

        return authCodeRepository.save(authCodeHistory);
    }

    /**
     * 조회 및 검증
     */
    @Transactional(readOnly = true)
    public void findByAutoCode(User user, String authCode) {
        AuthCodeHistory findAuthCode = authCodeRepository.findTop1ByUserAndAutoCodeOrderByExpireDateDesc(user, authCode)
                .orElseThrow(AuthCodeNotFoundException::new);

        if (!authCode.equals(findAuthCode.getAutoCode())) {
            throw new AuthCodeNoMatchException();
        }

        if (!user.equals(findAuthCode.getUser())) {
            throw new AuthCodeNoMatchException();
        }

        if (findAuthCode.getExpireDate().isBefore(LocalDateTime.now())) {
            throw new AuthCodeExpireException();
        }
    }

    /**
     * Auto 생성
     */
    private String createAuthCode() {
        return UUID.randomUUID().toString();
    }
}