package com.sparta.wildcard_newsfeed;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class WildcardNewsfeedApplication {

	public static void main(String[] args) {
		SpringApplication.run(WildcardNewsfeedApplication.class, args);
	}

}
