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
import com.sparta.wildcard_newsfeed.domain.user.controller.UserController;
import com.sparta.wildcard_newsfeed.domain.user.dto.EmailSendEvent;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserEmailRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupRequestDto;
import com.sparta.wildcard_newsfeed.domain.user.dto.UserSignupResponseDto;
import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.entity.UserRoleEnum;
import com.sparta.wildcard_newsfeed.domain.user.service.AuthCodeService;
import com.sparta.wildcard_newsfeed.domain.user.service.MailService;
import com.sparta.wildcard_newsfeed.domain.user.service.UserService;
import com.sparta.wildcard_newsfeed.mvc.MockSpringSecurityFilter;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import com.sparta.wildcard_newsfeed.security.AuthenticationUserService;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.context.WebApplicationContext;

import java.security.Principal;
import java.util.Set;
import java.util.UUID;

import static net.sf.jsqlparser.util.validation.metadata.NamedObject.user;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = {UserController.class},
        excludeFilters = {
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = WebSecurityConfig.class
                )
        }
)
class UserControllerTest {

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
    AuthenticationUserService authenticationUserService;

    @MockBean
    AuthCodeService authCodeService;

    @MockBean
    MailService mailService;

    @BeforeEach
    public void setup() {
        mvc = MockMvcBuilders.webAppContextSetup(context)
                .apply(springSecurity(new MockSpringSecurityFilter()))
                .build();
    }


    private void mockTestUserDtoSetup() {
        // Mock 테스트 유저 생성
        User testUser = User.builder()
                .usercode("HelloWorld1234")
                .password("HelloWorld1234!")
                .name("HelloWorld1234")
                .email("sinmk99@naver.com")
                .userRoleEnum(UserRoleEnum.USER)
                .build();
        AuthenticationUser authenticationUser = AuthenticationUser.of(testUser);
        mockPrincipal = new UsernamePasswordAuthenticationToken(authenticationUser, "", authenticationUser.getAuthorities());
    }

    @Test
    @DisplayName("회원 가입 성공")
    void test1() throws Exception {
        // given
        UserSignupRequestDto userSignupRequestDto = new UserSignupRequestDto("HelloWorld1234", "HelloWorld1234!", "sinmk99@naver.com");
        UserSignupResponseDto userSignupresponseDto = new UserSignupResponseDto("HelloWorld1234", "sinmk99@naver.com");

        when(userService.signup(any(UserSignupRequestDto.class))).thenReturn(userSignupresponseDto);

        // when - then
        mvc.perform(post("/api/v1/user/signup")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userSignupRequestDto))
                )
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("회원가입 성공"))
                .andExpect(jsonPath("$.data.usercode").value(userSignupRequestDto.getUsercode()))
                .andExpect(jsonPath("$.data.email").value(userSignupRequestDto.getEmail()));
    }

    @Test
    @DisplayName("이메일 인증번호 검증")
    void test2() throws Exception {

        // given

        String authCode = userService.createAuthCode();
        UserEmailRequestDto userEmailRequestDto = new UserEmailRequestDto();
        userEmailRequestDto.setAuthCode(authCode);

        when(authCode.equals(jsonPath("$.statusCode"))).thenReturn(Boolean.valueOf(userEmailRequestDto.getAuthCode()));

        // when & then
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/email/verify")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(userEmailRequestDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.statusCode").value(HttpStatus.OK.value()))
                .andExpect(jsonPath("$.message").value("인증번호 검증 성공"))
                .andExpect(jsonPath("$.data.authCode").value(authCode));
    }


    @Test
    @DisplayName("토큰 재발급")
    void test4() throws Exception {
        // given
        String refreshToken = "mockRefreshToken";
        String newToken = "newMockToken";
        //String nowToken = tokenService.;

        //when().thenReturn(newToken);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("refreshToken", refreshToken);

        // when & then
        mvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/reissue")
                        .params(params)
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().isOk())
                .andExpect(content().string(newToken))
                .andDo(print());
    }
}
