package com.movie.review;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableAsync;

@SpringBootApplication
public class MoviesReviewServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(MoviesReviewServiceApplication.class, args);
	}

}
