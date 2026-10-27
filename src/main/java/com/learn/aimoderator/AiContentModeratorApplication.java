package com.learn.aimoderator;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
// Exercise 3: needs the spring-boot-starter-cache dependency (see pom.xml).
// import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
public class AiContentModeratorApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiContentModeratorApplication.class, args);
    }
}
