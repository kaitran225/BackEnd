package com.healthy.backend.dto.survey.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.survey.QuestionOption;
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
public class SurveyQuestionResultResponse {
    @Schema(example = "SQR001")
    private String questionId;
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
    private List<StatusStudentResponse> ansStudent;


}