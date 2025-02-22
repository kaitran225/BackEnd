package com.healthy.backend.service;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
public class PsychologistStatusInitializer {

    private final PsychologistService psychologistService;

    public PsychologistStatusInitializer(PsychologistService psychologistService) {
        this.psychologistService = psychologistService;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void updatePsychologistStatusOnStartup() {
        psychologistService.updatePsychologistStatusBasedOnLeaveRequests();
    }
}