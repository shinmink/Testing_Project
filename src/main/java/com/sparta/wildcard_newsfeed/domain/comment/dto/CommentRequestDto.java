package com.sparta.wildcard_newsfeed.domain.comment.dto;

import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import lombok.Getter;

@Getter
public class CommentRequestDto {

    private String comment;
    private User user;

    public CommentRequestDto(String comment, User user) {
        this.comment = comment;
        this.user = user;
    }
}