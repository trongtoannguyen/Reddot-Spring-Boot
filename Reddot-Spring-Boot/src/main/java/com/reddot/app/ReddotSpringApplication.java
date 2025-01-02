package com.reddot.app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class ReddotSpringApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReddotSpringApplication.class, args);
    }

}
