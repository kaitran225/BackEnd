package com.healthy.backend.dto.survey.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveysResponse {
    @Schema(example = "SUR001")
    private String id;
    @Schema(example = "Example Survey")
    private String title;
    @Schema(example = "Survey Description")
    private String description;
    @Schema(example = "PSS_10")
    private String standardType;
    @Schema(example = "15")
    private Integer numberOfQuestions;
    @Schema(example = "2")
    private Integer periodic;
    @Schema(example = "Anxiety")
    private String category;
    @Schema(example = "Finished")
    private String status;
    @Schema(example = "2023-01-01")
    private String createdAt;
    @Schema(example = "US001")
    private String createBy;
    @Schema(example = "")
    private String completeStatus;
    @Schema(example = "")
    private List<StatusStudentResponse> statusStudentResponse;
    @Schema(example = "")
    private String score;
    @Schema(example = "")
    private List<SurveyQuestionResultResponse> questions;
    @Schema(example = "")
    private String studentComplete;
}
