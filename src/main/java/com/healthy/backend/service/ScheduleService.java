package com.healthy.backend.service;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class ScheduleService {

    private final ProgramService programService;
    private final SurveyService surveyService;

    @EventListener(ApplicationReadyEvent.class)
    @Transactional
    public void onApplicationStart() {
        System.out.println("Checking program statuses at startup...");
        programService.updateProgramStatuses();
        System.out.println("Sending program reminders...");
        programService.sendProgramReminders();
        System.out.println("Checking survey statuses at startup...");
        surveyService.updatePeriodicBulk();
    }

    // Run every day at midnight
    @Scheduled(cron = "0 0 0 * * *")
    @Transactional
    public void scheduledStatusUpdate() {
        System.out.println("Checking program statuses at midnight...");
        programService.updateProgramStatuses();
        System.out.println("Sending program reminders...");
        programService.sendProgramReminders();
        System.out.println("Checking survey statuses at startup...");
        surveyService.updatePeriodicBulk();
    }
}
