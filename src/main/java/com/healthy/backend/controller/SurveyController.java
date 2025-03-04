package com.healthy.backend.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.healthy.backend.dto.survey.QuestionOption;
import com.healthy.backend.dto.survey.StatusStudent;
import com.healthy.backend.dto.survey.SurveyQuestionResponse;
import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyRequest;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.survey.SurveysResponse;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.service.SurveyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Survey Controller", description = "Survey related APIs")
public class SurveyController {

    private final SurveyService surveyService;

    @Operation(
            summary = "Get all surveys",
            description = "Returns a list of available surveys."
    )
    @GetMapping()
    public ResponseEntity<List<SurveysResponse>> getAllSurveys(HttpServletRequest request) {
        List<SurveysResponse> surveys = surveyService.getAllSurveys(request);
        if (surveys.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(surveys);      
    }

    @Operation(
            summary = "Get score in survey",
            description = "Return the score that the student achieved in the survey"
    )
    @PostMapping("/{surveyId}/{studentId}/options/scoreResult")
    public ResponseEntity<?> getScoreFromStudentInSuv(@PathVariable String surveyId, @RequestBody List<String> optionId, @PathVariable String studentId) {
// api test [
//   "ANS003", "ANS005", "ANS010", "ANS014", "ANS018", "ANS022", "ANS026", "ANS030", "ANS034", "ANS039"
// ]
        try {
            StatusStudent status = surveyService.getScoreFromStudentInSuv(surveyId, optionId, studentId);
            return ResponseEntity.ok(status);
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while getting score in the survey " + ex.getMessage());
        }
    }



    @Operation(
            summary = "Get survey details",
            description = "Returns details for a specific survey."
    )
    @GetMapping("/{surveyId}/questions")
    public ResponseEntity<?> getSurveyDetails(@PathVariable String surveyId) {
        SurveyQuestionResponse surveyQuestions = surveyService.getSurveyQuestion(surveyId);
        if (surveyQuestions == null) {
            throw new ResourceNotFoundException("No survey questions found");
        }
        return ResponseEntity.ok(surveyQuestions);
    }

    @Operation(
            summary = "Update question in survey",
            description = "Updates a question in a survey."
    )
    @PutMapping("/{surveyId}/questions")
    public ResponseEntity<?> updateSurveyQuestion(
            @PathVariable String surveyId,
            @Valid @RequestBody SurveyQuestionResponse surveyQuestionResponse

    ) {
        try {
            surveyService.updateSurveyQuestion(surveyId, surveyQuestionResponse);
            return ResponseEntity.ok("Survey question updated sucessfully");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating the survey question" + ex.getMessage());
        }
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

            summary = "Get survey results",
            description = "Returns results for a specific survey."
    )
    @GetMapping("/{surveyId}/result")
    public ResponseEntity<?> getSurveyResults(HttpServletRequest request, @PathVariable String surveyId) {
        try {
                SurveyResultsResponse surveyResult = surveyService.getSurveyResults(request, surveyId);
                return ResponseEntity.ok(surveyResult);
            }
            catch(ResourceNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            }
            catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while getting the surveyResult " + ex.getMessage());
            }
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
            summary = "Create survey",
            description = "Creates a new survey."
    )
    @PostMapping("/create")
    public ResponseEntity<SurveyRequest> createSurvey(
            @Valid @RequestBody SurveyRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(new SurveyRequest());
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
            summary = "Add question to survey",
            description = "Adds a question to a survey."
    )
    @PostMapping("/{surveyId}/questions")
    public ResponseEntity<?> addSurveyQuestion(HttpServletRequest request, @PathVariable String surveyId, @RequestBody SurveyQuestionResponse question) {
        try {
                surveyService.addSurveyQuestion(request, surveyId, question);
                return ResponseEntity.ok("Survey question add sucessfully");
            }
            catch(ResourceNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            }
            catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding the survey question" + ex.getMessage());
            }
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
            summary = "Add answer to question",
            description = "Adds an answer to a question."
    )
    @PostMapping("/{surveyId}/questions/{questionId}/answers")
    public ResponseEntity<?> addAnswerToQuestion(@PathVariable String surveyId, @PathVariable String questionId, @RequestBody List<QuestionOption> answer) {
            try {
                surveyService.addAnswerToQuestion(surveyId, questionId, answer);;
                return ResponseEntity.ok("List of answers add sucessfully");
            }
            catch(ResourceNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            }
            catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while adding list of answers  " + ex.getMessage());
            }
    }

    @Operation(
        
            summary = "Get student survey results",
            description = "Returns results for a specific student's survey."
    )
    @GetMapping("/{surveyId}/students-results/{studentId}")
    public ResponseEntity<?> getStudentIDSurveyResults(HttpServletRequest request, @PathVariable String surveyId, @PathVariable String studentId) {
            try {
                SurveyResultsResponse surveyResponse = surveyService.getStudentIDSurveyResults(request, surveyId, studentId);
                return ResponseEntity.ok(surveyResponse);
            }
            catch(ResourceNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            }
            catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while getting student of survey" + ex.getMessage());
            }
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
            summary = "Update survey status",
            description = "Updates the status of a survey."
    )
    @PutMapping("/{surveyId}/status")
    public ResponseEntity<?> updateSurveyStatus(@PathVariable String surveyId, @RequestBody SurveyRequest status) {
        try {
                surveyService.updateSurveyStatus(surveyId, status);
                return ResponseEntity.ok("Survey status updated sucessfully");
            }
            catch(ResourceNotFoundException ex) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
            }
            catch (Exception ex) {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while updating status of survey" + ex.getMessage());
            }
        
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
