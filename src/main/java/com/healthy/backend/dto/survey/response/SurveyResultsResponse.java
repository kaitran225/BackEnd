package com.healthy.backend.dto.survey.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyResultsResponse {
    @Schema(example = "SUR001")
    private String surveyId;
    @Schema(example = "Example Survey")
    private String surveyName;
    @Schema(example = "Survey Description")
    private String description;
    @Schema(example = "")
    private String studentId;
    @Schema(example = "PSS_10")
    private String standardType;
    @Schema(example = "2")
    private Integer periodic;
    @Schema(example = "Anxiety")
    private String category;
    @Schema(example = "")
    private String status;
    @Schema(example = "2023-01-01")
    private LocalDateTime startDate;
    @Schema(example = "2023-01-01")
    private LocalDateTime endDate;
    @Schema(examples = {"Question 1", "Question 2", "Question 3", "Question 4", "Question 5"})
    private List<SurveyQuestionResultResponse> questions;
    @Schema(example = "")
    private List<StatusStudentResponse> std;
    @Schema(example = "")
    private List<StatusStudentResponse> totalScore;
}
