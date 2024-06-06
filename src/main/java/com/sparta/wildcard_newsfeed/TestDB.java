package com.sparta.wildcard_newsfeed;

import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.entity.UserRoleEnum;
import com.sparta.wildcard_newsfeed.domain.user.entity.UserStatusEnum;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class TestDB {

    private final InitService initService;


    @PostConstruct
    public void init() {
        initService.dbInit1();
    }

    @Component
    @Transactional
    @RequiredArgsConstructor
    static class InitService {
        private final EntityManager em;

        @Autowired
        PasswordEncoder passwordEncoder;

        public void dbInit1() {
            User user = User.builder()
                    .usercode("testid1234")
                    .password(passwordEncoder.encode("currentPWD999!"))
                    .name("B15_user1")
                    .email("test@naver.com")
                    .introduce("B15_user1의 하고 싶은 말")
                    .userRoleEnum(UserRoleEnum.USER)
                    .userStatus(UserStatusEnum.ENABLED)
                    .authUserAt(LocalDateTime.now())
                    .userRoleEnum(UserRoleEnum.USER)
                    .build();
            save(user);

        }

        public void save(Object... objects) {
            for (Object object : objects) {
                em.persist(object);
            }
        }
    }
}

