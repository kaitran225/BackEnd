package com.healthy.backend.controller;

import com.healthy.backend.dto.survey.request.ConfirmationRequest;
import com.healthy.backend.dto.survey.request.SurveyRequest;
import com.healthy.backend.dto.survey.request.SurveyUpdateRequest;
import com.healthy.backend.dto.survey.response.StatusStudentResponse;
import com.healthy.backend.dto.survey.response.SurveyQuestionResponse;
import com.healthy.backend.dto.survey.response.SurveyResultsResponse;
import com.healthy.backend.dto.survey.response.SurveysResponse;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.security.TokenService;
import com.healthy.backend.service.SurveyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Survey Controller", description = "Survey related APIs")
public class SurveyController {

    private final SurveyService surveyService;
    private final TokenService tokenService;

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Get all surveys",
            description = "Returns a list of available surveys."
    )
    @GetMapping()
    public ResponseEntity<List<SurveysResponse>> getAllSurveys(HttpServletRequest request) {
        Users user = tokenService.retrieveUser(request);
        List<SurveysResponse> surveys = surveyService.getAllSurveys(user);
        if (surveys.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(surveys);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Submit survey and get score",
            description = "Return the score that the student achieved in the survey"
    )
    @PostMapping("/submit")
    public ResponseEntity<StatusStudentResponse> getScoreFromStudentInSuv(
            @RequestParam String surveyId,
            @RequestBody List<String> optionId,
            @RequestParam(required = false) String studentId) {
        StatusStudentResponse status = surveyService.getScoreFromStudentInSuv(surveyId, optionId, studentId);
        return ResponseEntity.ok(status);
    }

    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Get survey question for taking survey",
            description = "Returns details for a specific survey."
    )
    @GetMapping("/questions")
    public ResponseEntity<?> getSurveyDetails(@RequestParam String surveyId) {
        SurveyQuestionResponse surveyQuestions = surveyService.getSurveyQuestion(surveyId);
        if (surveyQuestions == null) {
            throw new ResourceNotFoundException("No survey questions found");
        }
        return ResponseEntity.ok(surveyQuestions);
    }

    // update survey
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Update question in survey",
            description = "Updates a question in a survey."
    )
    @PutMapping("/update")
    public ResponseEntity<?> updateSurvey(
            HttpServletRequest request,
            @RequestParam String surveyId,
            @Valid @RequestBody SurveyUpdateRequest surveyRequest
    ) {
        if (tokenService.isManager(request) || tokenService.isPsychologist(request)) {
            surveyService.updateSurvey(surveyId, surveyRequest);
            return ResponseEntity.ok("Survey question updated successfully");
        }
        throw new AccessDeniedException("You do not have permission to update this survey");
    }

    // create survey
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Create survey",
            description = "Creates a new survey"
    )
    @PostMapping("/create")
    public ResponseEntity<?> createSurvey(
            HttpServletRequest request,
            @RequestBody SurveyRequest surveyRequest) {
        if (tokenService.isManager(request) || tokenService.isPsychologist(request)) {
            Users user = tokenService.retrieveUser(request);
            surveyService.createSurvey(surveyRequest, user);
            return ResponseEntity.ok("Survey question add successfully");
        }
        throw new OperationFailedException("You don't have permission to add question to this survey");
    }

    // Get survey results
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Get survey results",
            description = "Returns results for a specific survey."
    )
    @GetMapping("/result")
    public ResponseEntity<?> getSurveyResults(HttpServletRequest request) {
        List<SurveyResultsResponse> surveyResult = surveyService.getSurveyResultsBySurveyID(
                tokenService.retrieveUser(request));
        return ResponseEntity.ok(surveyResult);
    }

    // Get details of student survey results
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Get student survey results details",
            description = "Returns detail results for a specific student's survey."
    )
    @GetMapping("/results/student")
    public ResponseEntity<?> getStudentIDSurveyResults(
            HttpServletRequest request, @RequestParam String surveyId,
            @RequestParam(required = false) String studentId) {
        String studentID = tokenService.validateRequestStudentID(request, studentId);
        if(!studentID.equals(studentId)){
            throw new AccessDeniedException("Access Denied");
        }
        SurveyQuestionResponse surveyResponse = surveyService.getSurveyResultByStudentID(surveyId, studentID);
        return ResponseEntity.ok(surveyResponse);
    }

    // Deactivate survey
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Update survey",
            description = "Updates details of a survey."
    )
    @PutMapping("/deactivate")
    public ResponseEntity<?> deactivateSurvey(HttpServletRequest request, @RequestParam String surveyId) {
        if (tokenService.isManager(request) || tokenService.isPsychologist(request)) {
            surveyService.deactivateSurvey(surveyId);
            return ResponseEntity.ok("Survey deactivated successfully");
        }
        throw new AccessDeniedException("Access Denied");
    }

    // Activate survey
    @SuppressWarnings("unused")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Update survey",
            description = "Updates details of a survey."
    )
    @PutMapping("/activate")
    public ResponseEntity<?> activateSurvey(HttpServletRequest request, @RequestParam String surveyId) {
        if (tokenService.isManager(request) || tokenService.isPsychologist(request)) {
            surveyService.activateSurvey(surveyId);
            return ResponseEntity.ok("Survey activated successfully");
        }
        throw new AccessDeniedException("Access Denied");
    }

    // Get all standard types
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Get all survey standard types",
            description = "Returns a list of all survey standard types."
    )
    @GetMapping("/standardized")
    public ResponseEntity<?> getAllStandardTypes() {
        return ResponseEntity.ok(surveyService.getAllStandardTypes());
    }

    // Get all categories
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @Operation(
            summary = "Export survey results",
            description = "Exports survey results in a specified format."
    )
    @GetMapping("/categories")
    public ResponseEntity<?> getAllCategories() {
        return ResponseEntity.ok(surveyService.getAllCategories());
    }

    // Get low scoring students
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @GetMapping("/low-scoring-check")
    public ResponseEntity<?> getLowScoringStudentsForAppointment(HttpServletRequest request) {
        if (tokenService.isStudent(request)) {
            ConfirmationRequest confirmationRequests =
                    surveyService.getLowScoringStudentsForAppointment(tokenService.retrieveUser(request));
            return ResponseEntity.ok(confirmationRequests);
        }
        throw new AccessDeniedException("Access Denied");
    }

    //Confirm appointment suggestion
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Successful",
                    content = @Content(schema = @Schema(hidden = true))),
            @ApiResponse(responseCode = "500", description = "Internal Server Error",
                    content = @Content(schema = @Schema(hidden = true)))
    })
    @PostMapping("/appointment-suggestion")
    public ResponseEntity<?> handleAppointmentRequest(@RequestBody ConfirmationRequest requests) {
        try {
            return ResponseEntity.ok(surveyService.handleAppointmentRequest(requests) ? "You can make appointment now" : "");
        } catch (ResourceNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An error occurred while processing the request" + ex.getMessage());
        }
    }
}
