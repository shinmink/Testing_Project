package com.sparta.wildcard_newsfeed.aop;

import com.sparta.wildcard_newsfeed.domain.user.entity.User;
import com.sparta.wildcard_newsfeed.domain.user.repository.UserRepository;
import com.sparta.wildcard_newsfeed.security.AuthenticationUser;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Slf4j(topic = "ApiRequestLog")
@Aspect
@Component
@RequiredArgsConstructor
public class ApiRequestLogAop {

    private final ApiUseTimeRepository apiUseTimeRepository;
    private final UserRepository userRepository;

    @Pointcut("execution(* com.sparta.wildcard_newsfeed.domain.comment.controller.CommentController.*(..))")
    private void comment() {}
    @Pointcut("execution(* com.sparta.wildcard_newsfeed.domain.liked.controller.LikedController.*(..))")
    private void liked() {}
    @Pointcut("execution(* com.sparta.wildcard_newsfeed.domain.post.controller.PostController.*(..))")
    private void post() {}
    @Pointcut("execution(* com.sparta.wildcard_newsfeed.domain.token.controller.TokenController.*(..))")
    private void token() {}
    @Pointcut("execution(* com.sparta.wildcard_newsfeed.domain.user.controller.UserController.*(..))")
    private void user() {}
    @Pointcut("execution(* com.sparta.wildcard_newsfeed.domain.user.controller.EmailController.*(..))")
    private void email() {}

    @Around("comment() || liked() || post() || token() || user() || email()")
    public Object logRequestDetails(ProceedingJoinPoint joinPoint) throws Throwable {
        // 현재 요청의 HttpServletRequest를 가져옴
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getRequest();

        // 요청 URL과 HTTP 메서드 로그 출력
        String requestUrl = request.getRequestURL().toString();
        String httpMethod = request.getMethod();
        log.info("Request URL: {}, HTTP Method: {}", requestUrl, httpMethod);

        // 측정 시작 시간
        long startTime = System.currentTimeMillis();

        try {
            // 핵심기능 수행
            Object output = joinPoint.proceed();
            return output;
        } finally {
            // 측정 종료 시간
            long endTime = System.currentTimeMillis();
            // 수행시간 = 종료 시간 - 시작 시간
            long runTime = endTime - startTime;

            // 로그인 토큰이 없는 경우, 수행시간 기록하지 않음
            Authentication auth = SecurityContextHolder.getContext().getAuthentication();
            if (auth != null && auth.getPrincipal().getClass() == AuthenticationUser.class) {
                // 로그인 회원 정보
                AuthenticationUser authenticationUser = (AuthenticationUser) auth.getPrincipal();
                User loginUser = userRepository.findByUsercode(authenticationUser.getUsername())
                        .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));

                // API 사용시간 및 DB에 기록
                ApiUseTime apiUseTime = apiUseTimeRepository.findByUser(loginUser).orElse(null);
                if (apiUseTime == null) {
                    // 로그인 회원의 기록이 없으면
                    apiUseTime = new ApiUseTime(loginUser, runTime);
                } else {
                    // 로그인 회원의 기록이 이미 있으면
                    apiUseTime.addUseTime(runTime);
                }

                log.info("[API Use Time] Username: " + loginUser.getName() + ", Total Time: " + apiUseTime.getTotalTime() + " ms");
                apiUseTimeRepository.save(apiUseTime);
            }
        }
    }
}
