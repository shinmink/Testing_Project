package com.sparta.wildcard_newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.wildcard_newsfeed.config.WebSecurityConfig;
import com.sparta.wildcard_newsfeed.domain.comment.controller.CommentController;
import com.sparta.wildcard_newsfeed.domain.comment.service.CommentService;
import com.sparta.wildcard_newsfeed.domain.post.controller.PostController;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostRequestDto;
import com.sparta.wildcard_newsfeed.domain.post.dto.PostResponseDto;
import com.sparta.wildcard_newsfeed.domain.post.service.PostService;
import com.sparta.wildcard_newsfeed.domain.token.service.TokenService;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.entity.UserRoleEnum;
import com.sparta.wildcard_newsfeed.domain.user.service.AuthCodeService;
import com.sparta.wildcard_newsfeed.domain.user.service.UserService;
import com.sparta.wildcard_newsfeed.mvc.MockSpringSecurityFilter;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
@WebMvcTest(
        controllers = {PostController.class, CommentController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class PostControllerTest {

    private MockMvc mvc;

    private Principal mockPrincipal;

    @Autowired
    private WebApplicationContext context;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    PostService postService;

    @MockBean
    CommentService commentService;

    @MockBean
    TokenService tokenService;

    @MockBean
    UserService userService;


    @MockBean
    AuthCodeService authCodeService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }

    private AuthenticationUser mockUserDtoSetup() {
        // Mock 테스트 유저 생성
        String usercode = "HelloWorld1234";
        String password = "HelloWorld1234!";
        String username = "HelloWorld1234";
        String email = "sinmk99@naver.com";
        UserRoleEnum role = UserRoleEnum.USER;
        User testUser = new User(usercode, password, email);
        AuthenticationUser authenticationUser;
        authenticationUser = AuthenticationUser.of(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(authenticationUser, "", authenticationUser.getAuthorities());

        return authenticationUser;
    }



    @Test
    @WithMockUser
    @DisplayName("게시물 작성 테스트")
    public void testAddPost() throws Exception {

        // given
//        AuthenticationUser authenticationUser;
//        authenticationUser = mockUserDtoSetup();
//
//        PostRequestDto requestDto = new PostRequestDto("Title", "Content", null);
//        PostResponseDto responseDto = new PostResponseDto(1L, "Title", "Content", authenticationUser);
//
//        Mockito.when(postService.addPost(any(PostRequestDto.class), any(AuthenticationUser.class)))
//                .thenReturn(responseDto);
//
//        mvc.perform(post("/api/v1/post")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content(objectMapper.writeValueAsString(requestDto))
//                        .principal(() -> "testUser"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.message").value("게시물 등록 성공"))
//                .andExpect(jsonPath("$.data.title").value("Title"))
//                .andExpect(jsonPath("$.data.content").value("Content"))
//                .andExpect(jsonPath("$.data.username").value("testUser"));

    }

    @Test
    @WithMockUser
    @DisplayName("게시물 전체 조회 테스트")
    public void testFindAll() throws Exception {

    }

    @Test
    @WithMockUser
    @DisplayName("게시물 단일 조회 + 댓글 전체 조회 테스트")
    public void testFindById() throws Exception {

    }

    @Test
    @WithMockUser
    @DisplayName("게시물 수정 테스트")
    public void testUpdatePost() throws Exception {

    }

    @Test
    @WithMockUser
    @DisplayName("게시물 삭제 테스트")
    public void testDeletePost() throws Exception {

    }


}