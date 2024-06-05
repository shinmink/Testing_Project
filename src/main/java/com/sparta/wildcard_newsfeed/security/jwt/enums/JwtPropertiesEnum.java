package com.sparta.wildcard_newsfeed.security.jwt.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum JwtPropertiesEnum {
    INVALID_TOKEN("유효하지 않는 JWT 서명 입니다."),
    EXPIRED_JWT_TOKEN("만료된 JWT token 입니다."),
    UNSUPPORTED_JWT_TOKEN("지원되지 않는 JWT 토큰 입니다."),
    JWT_CLAIMS_IS_EMPTY("잘못된 JWT 토큰 입니다."),
    ;

    private final String errorMessage;

    public static JwtPropertiesEnum fromJwtProperties(String fileExtension) {
        JwtPropertiesEnum jwtPropertiesEnum = JWT_CLAIMS_IS_EMPTY; //Default message
        for (JwtPropertiesEnum value : values()) {
            if (value.getErrorMessage().equalsIgnoreCase(fileExtension)) {
                jwtPropertiesEnum = value;
            }
        }
        return jwtPropertiesEnum;
    }
}
