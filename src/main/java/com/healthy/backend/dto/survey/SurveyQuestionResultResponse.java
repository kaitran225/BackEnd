package com.healthy.backend.dto.survey;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyQuestionResultResponse {
    @Schema(example = "SQR001")
    private String questionId;
    @Schema(example = "Anxiety")
    private String categoryName;
    @Schema(example = "How often do you have panic attacks?")
    private String questionText;
    @Schema(example = "SUR002")
    private String resultId;
    @Schema(example = "SQO001")
    private String answerId;
    @Schema(example = "Good")
    private String answer;
    @Schema(example = "1")
    private Integer score;
    @Schema(example = "")
    private List<QuestionOption> answers;
    @Schema(example = "")
    private List<StatusStudent> ansStudent;


}