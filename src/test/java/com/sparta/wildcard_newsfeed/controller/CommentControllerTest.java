package com.sparta.wildcard_newsfeed.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sparta.wildcard_newsfeed.domain.comment.controller.CommentController;
import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentRequestDto;
import com.sparta.wildcard_newsfeed.domain.comment.dto.CommentResponseDto;
import com.sparta.wildcard_newsfeed.domain.comment.service.CommentService;
import com.sparta.wildcard_newsfeed.domain.common.CommonResponseDto;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


@WebMvcTest(CommentController.class)
class CommentControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CommentService commentService;



    @Test
    @WithMockUser
    @DisplayName("댓글 작성 테스트")
    public void testAddComment() throws Exception {
        mockMvc.perform(post("/api/v1/post/{postId}/comment"));

    }

    @Test
    @WithMockUser
    @DisplayName("댓글 수정 테스트")
    public void testUpdateComment() throws Exception {
        mockMvc.perform(get("/api/v1/post/{postId}/comment/{commentId}"));



    }

    @Test
    @WithMockUser
    @DisplayName("댓글 삭제 테스트")
    public void testDeleteComment() throws Exception {
        mockMvc.perform(get("/api/v1/post/{postId}/comment/{commentId}"));



    }

}