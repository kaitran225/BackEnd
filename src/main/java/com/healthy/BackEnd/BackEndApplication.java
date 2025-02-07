package com.healthy.BackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackEndApplication {

    public static void main(String[] args) {
        System.out.println("url" + System.getenv("DATABASE_URL"));
        SpringApplication.run(BackEndApplication.class, args);
    }

}
