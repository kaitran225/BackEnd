package com.healthy.backend.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemConfig {

//    @Scheduled(cron = "0 0 12 * * ?")
    @Scheduled(cron = "0 22 10 * * ?")
    public void shutdownAtNoon() {
        System.out.println("Shutting down the application at noon...");
        System.exit(0);
    }
}
