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
public class SurveyQuestionResponse {
    @Schema(example = "SUR001")
    private String surveyId;
    @Schema(example = "How are you feeling today?")
    private String title;
    @Schema(example = "")
    private String description;
    @Schema(example = "PSS_10")
    private String standardType;
    @Schema(example = "2")
    private Integer periodic;
    @Schema(example = "")
    private String periodicID;
    @Schema(example = "Anxiety")
    private String category;
    @Schema(example = "2023-01-01")
    private LocalDateTime startDate;
    @Schema(example = "2023-01-01")
    private LocalDateTime endDate;
    @Schema(example = "")
    private Integer numberOfQuestions;
    @Schema(example = "")
    private String completeStatus;
    @Schema(example = "")
    private String totalScore;
    @Schema(example = "")
    private List<QuestionResponse> questionList;
}
