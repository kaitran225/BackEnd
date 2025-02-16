package com.healthy.backend.controller;

import java.util.List;
import java.util.stream.Collectors;

import com.healthy.backend.dto.survey.SubmitSurveyRequest;
import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.entity.SurveyQuestions;
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

import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.service.SurveyService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequestMapping("/api/surveys")
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Survey Controller", description = "Survey related APIs")
public class SurveyController {
    private final SurveyService surveyService;
    // Thêm vào class SurveyController
    @Operation(summary = "Lấy danh sách câu hỏi của khảo sát")
    @GetMapping("/{surveyId}/questions")
    public ResponseEntity<List<SurveyQuestionResultResponse>> getSurveyQuestions(
            @PathVariable String surveyId
    ) {
        List<SurveyQuestions> questions = surveyService.getQuestionsBySurveyId(surveyId);
        return ResponseEntity.ok(
                questions.stream()
                        .map(surveyService::mapToQuestionResponse)
                        .collect(Collectors.toList())
        );
    }

    @Operation(summary = "Submit kết quả khảo sát")
    @PostMapping("/{surveyId}/submit")
    public ResponseEntity<String> submitSurvey(
            @PathVariable String surveyId,
            @RequestBody SubmitSurveyRequest request
    ) {
        surveyService.submitSurveyResults(surveyId, request);
        return ResponseEntity.ok("Survey submitted successfully");
    }

}
