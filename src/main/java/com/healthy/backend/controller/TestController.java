package com.healthy.backend.controller;

import com.healthy.backend.service.ProgramService;
import com.healthy.backend.service.SurveyService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/test")
@Tag(name = "Test Controller", description = "Test APIs for demo purposes")
public class TestController {
    private final ProgramService programService;
    private final SurveyService surveyService;

    @PutMapping("/survey-new-periodic-update")
    public ResponseEntity<?> updateSurveys(@RequestParam String surveyId) {
        surveyService.periodicUpdateSurvey(surveyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/survey-reset-periodic-auto")
    public ResponseEntity<?> resetSurveys() {
        surveyService.updatePeriodicBulk();
        return ResponseEntity.ok().build();
    }

    @PutMapping("/survey-rest-periodic")
    public ResponseEntity<?> resetSurveys(
            @RequestParam String surveyId,
            @RequestParam String periodID) {
        surveyService.periodicResetSurvey(surveyId, periodID);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/program-update")
    public ResponseEntity<?> updateProgram() {
        programService.updateProgramStatuses();
        return ResponseEntity.ok().build();
    }


    @PostMapping("/program-send-reminders")
    public ResponseEntity<?> sendProgramReminders() {
        programService.sendProgramReminders();
        return ResponseEntity.ok().build();
    }

    @PostMapping("/survey-repeat")
    public ResponseEntity<?> sendProgramReminder(@RequestParam String studentId,
                                                 @RequestParam String surveyId) {
        surveyService.repeatSurvey(surveyId, studentId);
        return ResponseEntity.ok("Success");
    }
}
