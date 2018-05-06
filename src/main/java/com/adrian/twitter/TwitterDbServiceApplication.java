package com.adrian.twitter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class TwitterDbServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(TwitterDbServiceApplication.class, args);
	}
}
