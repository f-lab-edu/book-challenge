package com.flab.book_challenge;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BookChallengeApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookChallengeApplication.class, args);
    }

}
