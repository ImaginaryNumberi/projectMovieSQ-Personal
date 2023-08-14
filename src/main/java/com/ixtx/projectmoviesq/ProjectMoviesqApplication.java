package com.ixtx.projectmoviesq;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class ProjectMoviesqApplication {

    public static void main(String[] args) {

        SpringApplication.run(ProjectMoviesqApplication.class, args);
    }
}