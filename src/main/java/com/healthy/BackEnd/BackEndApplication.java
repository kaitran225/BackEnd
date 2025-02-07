package com.healthy.BackEnd;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class BackEndApplication {

    public static void main(String[] args) {
        System.out.println("Database Password: " + System.getenv("DATABASE_PASSWORD"));
        System.out.println("Database Url: " + System.getenv("DATABASE_HOST") + ":" + System.getenv("DATABASE_PORT") + "/" + System.getenv("DATABASE_NAME"));
        SpringApplication.run(BackEndApplication.class, args);
    }

}
