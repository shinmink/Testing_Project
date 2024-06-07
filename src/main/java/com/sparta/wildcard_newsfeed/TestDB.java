package com.sparta.wildcard_newsfeed;

import com.sparta.wildcard_newsfeed.domain.comment.entity.Comment;
import com.sparta.wildcard_newsfeed.domain.post.entity.Post;
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
import java.util.ArrayList;
import java.util.List;

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
                    .userStatus(UserStatusEnum.UNAUTHORIZED)
                    .authUserAt(LocalDateTime.now())
                    .userRoleEnum(UserRoleEnum.USER)
                    .build();
            save(user);

            List<Post> postList = new ArrayList<>();
            for (int i = 0; i < 33; i++) {
                Post post = new Post();
                post.setUser(user);
                post.setTitle("테스트 제목" + i);
                post.setContent("테스트 내용" + i);
                post.setTestDateTime();
                postList.add(post);
            }
            savePosts(postList);

            List<Comment> commentList = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                Comment comment = Comment.builder()
                        .content("테스트 댓글 내용")
                        .user(user)
                        .post(postList.get(i))
                        .likeCount(0L)
                        .build();
                comment.testDataInit();
                commentList.add(comment);
            }
            saveComments(commentList);
        }

        public void save(Object... objects) {
            for (Object object : objects) {
                em.persist(object);
            }
        }

        public void savePosts(List<Post> list) {
            for (Post post : list) {
                em.persist(post);
            }
        }

        public void saveComments(List<Comment> list) {
            for (Comment comment : list) {
                em.persist(comment);
            }
        }
    }
}