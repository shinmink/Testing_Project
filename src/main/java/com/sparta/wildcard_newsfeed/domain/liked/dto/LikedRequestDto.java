package com.sparta.wildcard_newsfeed.domain.liked.dto;

import com.sparta.wildcard_newsfeed.domain.liked.entity.ContentsTypeEnum;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikedRequestDto {
    private Long contentsId;
    private ContentsTypeEnum contentsType;
}