package com.sparta.wildcard_newsfeed.security.jwt;

import com.sparta.wildcard_newsfeed.security.jwt.dto.TokenDto;
import com.sparta.wildcard_newsfeed.security.jwt.enums.JwtPropertiesEnum;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.security.Key;
import java.util.Base64;
import java.util.Date;
import java.util.UUID;

import static com.sparta.wildcard_newsfeed.security.jwt.JwtConstants.*;

@Slf4j(topic = "JwtUtil")
@Component
public class JwtUtil {

    @Value("${jwt.secret-key}") // Base64 Encode 한 SecretKey
    private String secret_key;
    private Key key;
    private final SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    @PostConstruct
    public void init() {
        byte[] bytes = Base64.getDecoder().decode(secret_key);
        key = Keys.hmacShaKeyFor(bytes);
    }

    public TokenDto generateAccessTokenAndRefreshToken(String username) {
        String accessToken = createAccessToken(username);
        String refreshToken = createRefreshToken(username);

        return TokenDto.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    // access Token 생성
    public String createAccessToken(String userCode) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setId(UUID.randomUUID().toString()) // JWT ID 설정
                        .setSubject(userCode) // 사용자 식별자값(ID)
                        .setIssuedAt(new Date(date.getTime())) // 생성시간
                        .setExpiration(new Date(date.getTime() + ACCESS_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .claim("tokenType", "access")
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // refresh Token 생성
    public String createRefreshToken(String username) {
        Date date = new Date();

        return BEARER_PREFIX +
                Jwts.builder()
                        .setId(UUID.randomUUID().toString()) // JWT ID 설정
                        .setSubject(username) // 사용자 식별자값(ID)
                        .setIssuedAt(new Date(date.getTime())) // 생성시간
                        .setExpiration(new Date(date.getTime() + REFRESH_TOKEN_TIME)) // 만료 시간
                        .setIssuedAt(date) // 발급일
                        .claim("tokenType", "refresh")
                        .signWith(key, signatureAlgorithm) // 암호화 알고리즘
                        .compact();
    }

    // header 에서 access token
    public String getAccessTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(ACCESS_TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // header 에서 refresh token
    public String getRefreshTokenFromHeader(HttpServletRequest request) {
        String bearerToken = request.getHeader(REFRESH_TOKEN_HEADER);
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith(BEARER_PREFIX)) {
            return bearerToken.substring(7);
        }
        return null;
    }

    // 토큰 검증
    public boolean validateToken(HttpServletRequest request, String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException | SignatureException e) {
            log.error("Invalid JWT signature, 유효하지 않는 JWT 서명");
            request.setAttribute("jwtException", JwtPropertiesEnum.INVALID_TOKEN.getErrorMessage());
        } catch (ExpiredJwtException e) {
            log.error("Expired JWT token, 만료된 JWT token");
            request.setAttribute("jwtException", JwtPropertiesEnum.EXPIRED_JWT_TOKEN.getErrorMessage());
        } catch (UnsupportedJwtException e) {
            log.error("Unsupported JWT token, 지원되지 않는 JWT 토큰");
            request.setAttribute("jwtException", JwtPropertiesEnum.UNSUPPORTED_JWT_TOKEN.getErrorMessage());
        } catch (IllegalArgumentException e) {
            log.error("JWT claims is empty, 잘못된 JWT 토큰");
            request.setAttribute("jwtException", JwtPropertiesEnum.JWT_CLAIMS_IS_EMPTY.getErrorMessage());
        }
        return false;
    }

    // 토큰에서 사용자 정보 가져오기
    public Claims getUserInfoFromToken(String token) {
        return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
    }
}