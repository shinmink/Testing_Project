package com.sparta.wildcard_newsfeed.domain.liked.dto;

import com.sparta.wildcard_newsfeed.domain.liked.entity.ContentsTypeEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class LikedRequestDto {
    @Schema(description = "컨텐츠 ID", example = "2")
    private Long contentsId;
    @Schema(description = "컨텐츠 타입 (POST || COMMENT)", example = "POST")
    private ContentsTypeEnum contentsType;
}