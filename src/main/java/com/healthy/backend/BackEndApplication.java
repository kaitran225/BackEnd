package com.healthy.backend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import java.util.Collections;

@SpringBootApplication(
        proxyBeanMethods = false
        // Disable proxy bean methods to reduce memory usage
)
@EnableScheduling
public class BackEndApplication {
    public static void main(String[] args) {
        SpringApplication app = new SpringApplication(BackEndApplication.class);
        // Set default properties for memory optimization
        app.setDefaultProperties(Collections.singletonMap("spring.main.banner-mode", "off"));
        app.run(args);
    }
}   