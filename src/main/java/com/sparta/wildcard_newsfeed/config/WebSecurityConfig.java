package com.sparta.wildcard_newsfeed.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;

@Configuration
@EnableWebSecurity/*(debug = true)*/
@RequiredArgsConstructor
public class WebSecurityConfig {
}