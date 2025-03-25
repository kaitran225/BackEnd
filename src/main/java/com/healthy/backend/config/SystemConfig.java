package com.healthy.backend.config;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class SystemConfig {

   @Scheduled(cron = "0 30 0 * * ?")
    public void shutdownAtNoon() {
        System.out.println("Shutting down the application at midnight...");
        System.exit(0);
    }
}
