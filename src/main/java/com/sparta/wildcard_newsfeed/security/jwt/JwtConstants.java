package com.sparta.wildcard_newsfeed.security.jwt;

public class JwtConstants {
    // Header KEY 값
    public static final String ACCESS_TOKEN_HEADER = "Authorization";
    public static final String REFRESH_TOKEN_HEADER = "Authorization-refresh";

    // Token 식별자
    public static final String BEARER_PREFIX = "Bearer ";
    // 토큰 만료시간
    public static final long ACCESS_TOKEN_TIME = 1000 * 60 * 30; // 30분
    public static final long REFRESH_TOKEN_TIME = 1000 * 60 * 60 * 24 * 14; // 2주
}
