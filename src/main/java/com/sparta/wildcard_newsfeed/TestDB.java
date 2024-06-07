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
            // 유저 1
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
            // 유저 2
            User user2 = User.builder()
                    .usercode("testid5678")
                    .password(passwordEncoder.encode("currentPWD999!"))
                    .name("B15_user2")
                    .email("test2@naver.com")
                    .introduce("B15_user2의 하고 싶은 말")
                    .userRoleEnum(UserRoleEnum.USER)
                    .userStatus(UserStatusEnum.UNAUTHORIZED)
                    .authUserAt(LocalDateTime.now())
                    .userRoleEnum(UserRoleEnum.USER)
                    .build();
            save(user2);

            List<Post> postList = new ArrayList<>();
            // 유저 1의 게시물
            initPosts(user, postList);
            // 유저 2의 게시물
            initPosts(user2, postList);
            savePosts(postList);

            List<Comment> commentList = new ArrayList<>();
            // 유저 1의 댓글
            initComments(user, postList, commentList);
            // 유저 2의 댓글
            initComments(user2, postList, commentList);
            saveComments(commentList);
        }

        private void initPosts(User user, List<Post> postList) {
            for (int i = 0; i < 33; i++) {
                Post post = new Post();
                post.setUser(user);
                post.setTitle("테스트 제목" + i);
                post.setContent("테스트 내용" + i);
                post.setLikeCount(0L);
                post.setTestDateTime();
                postList.add(post);
            }
        }

        private void initComments(User user, List<Post> postList, List<Comment> commentList) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    Comment comment = Comment.builder()
                            .content(user.getName() + "의 게시물" + i + "의 댓글" + j)
                            .user(user)
                            .post(postList.get(i))
                            .likeCount(0L)
                            .build();
                    comment.testDataInit();
                    commentList.add(comment);
                }
            }
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