package com.healthy.backend.config;

import jakarta.annotation.PostConstruct;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.time.ZoneId;
import java.time.ZonedDateTime;

@Component
public class SystemConfig {

//    @Scheduled(cron = "0 0 12 * * ?")
    @Scheduled(cron = "0 32 10 * * ?")
    public void shutdownAtNoon() {
        System.out.println("Shutting down the application at noon...");
        System.exit(0);
    }

    @PostConstruct
    public void logTimezone() {
        ZonedDateTime now = ZonedDateTime.now();
        System.out.println("Current server time: " + now);
        System.out.println("Current system timezone: " + ZoneId.systemDefault());
    }
}
