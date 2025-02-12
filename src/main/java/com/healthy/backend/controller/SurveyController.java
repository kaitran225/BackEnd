package com.healthy.backend.controller;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.service.ProgramService;
import com.healthy.backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Survey Controller", description = "Survey related APIs")
public class SurveyController {


    @Operation(
            deprecated = true,
            summary = "Get all surveys",
            description = "Returns a list of available surveys."
    )
    @GetMapping
    public List<String> getAllSurveys() {
        return List.of("List of available surveys");
    }

    @Operation(
            deprecated = true,
            summary = "Get survey details",
            description = "Returns details for a specific survey."
    )
    @GetMapping("/{surveyId}")
    public String getSurveyDetails(@PathVariable String surveyId) {
        return "Survey details for ID " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Submit survey response",
            description = "Submits a response to a survey."
    )
    @PostMapping("/{surveyId}/take")
    public String submitSurveyResponse(@PathVariable String surveyId, @RequestBody String responses) {
        return "Survey responses saved for survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Get survey results",
            description = "Returns results for a specific survey."
    )
    @GetMapping("/{surveyId}/result")
    public String getSurveyResults(@PathVariable String surveyId) {
        return "Survey results for ID " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Submit survey feedback",
            description = "Submits feedback for a survey."
    )
    @PostMapping("/{surveyId}/feedback")
    public String submitSurveyFeedback(@PathVariable String surveyId, @RequestBody String feedback) {
        return "Feedback submitted for survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Create survey",
            description = "Creates a new survey."
    )
    @PostMapping("/create")
    public String createSurvey(@RequestBody String surveyDetails) {
        return "New survey created successfully";
    }

    @Operation(
            deprecated = true,
            summary = "Update survey",
            description = "Updates an existing survey."
    )
    @PutMapping("/{surveyId}/update")
    public String updateSurvey(@PathVariable String surveyId, @RequestBody String surveyDetails) {
        return "Survey updated successfully";
    }

    @Operation(
            deprecated = true,
            summary = "Cancel survey",
            description = "Cancels a survey."
    )
    @DeleteMapping("/{surveyId}/cancel")
    public String cancelSurvey(@PathVariable String surveyId) {
        return "Survey canceled successfully";
    }

    @Operation(
            deprecated = true,
            summary = "Add question to survey",
            description = "Adds a question to a survey."
    )
    @PostMapping("/{surveyId}/questions")
    public String addSurveyQuestion(@PathVariable String surveyId, @RequestBody String question) {
        return "Question added to survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Update question in survey",
            description = "Updates a question in a survey."
    )
    @PutMapping("/{surveyId}/questions/{questionId}")
    public String updateSurveyQuestion(@PathVariable String surveyId, @PathVariable String questionId, @RequestBody String question) {
        return "Question updated for survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Delete question from survey",
            description = "Deletes a question from a survey."
    )
    @DeleteMapping("/{surveyId}/questions/{questionId}")
    public String deleteSurveyQuestion(@PathVariable String surveyId, @PathVariable String questionId) {
        return "Question removed from survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Add answer to question",
            description = "Adds an answer to a question."
    )
    @PostMapping("/{surveyId}/questions/{questionId}/answers")
    public String addAnswerToQuestion(@PathVariable String surveyId, @PathVariable String questionId, @RequestBody String answer) {
        return "Answer added to question " + questionId + " in survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Get student survey results",
            description = "Returns results for a specific student's survey."
    )
    @GetMapping("/{surveyId}/students-results")
    public String getStudentSurveyResults(@PathVariable String surveyId) {
        return "Student survey results for survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Get survey dashboard",
            description = "Returns a dashboard overview for surveys."
    )
    @GetMapping("/dashboard")
    public String getSurveyDashboard() {
        return "Survey dashboard overview";
    }

    @Operation(
            deprecated = true,
            summary = "Schedule survey",
            description = "Schedules a survey."
    )
    @PostMapping("/{surveyId}/schedule")
    public String scheduleSurvey(@PathVariable String surveyId, @RequestBody String scheduleDetails) {
        return "Survey " + surveyId + " scheduled successfully";
    }

    @Operation(
            deprecated = true,
            summary = "Update survey status",
            description = "Updates the status of a survey."
    )
    @PutMapping("/{surveyId}/status")
    public String updateSurveyStatus(@PathVariable String surveyId, @RequestBody String status) {
        return "Survey status updated for survey " + surveyId;
    }

    @Operation(
            deprecated = true,
            summary = "Enable anonymous survey",
            description = "Enables anonymous mode for a survey."
    )
    @PostMapping("/{surveyId}/anonymous")
    public String enableAnonymousSurvey(@PathVariable String surveyId) {
        return "Survey " + surveyId + " set to anonymous";
    }

    @Operation(
            deprecated = true,
            summary = "Export survey results",
            description = "Exports survey results in a specified format."
    )
    @GetMapping("/{surveyId}/export")
    public String exportSurveyResults(@PathVariable String surveyId, @RequestParam String format) {
        return "Survey results exported in format: " + format;
    }
}
