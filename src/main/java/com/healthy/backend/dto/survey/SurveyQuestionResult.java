package com.healthy.backend.dto.survey;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;



@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor


public class SurveyQuestionResult {
    @Schema(example = "Q001 ")
    private String questionID;

    @Schema(example = "Q001")
    private String questionText;

    @Schema(example = "A002")
    private String answerID;

    @Schema(example = "SUR001")
    private String surveyID;

    @Schema(example = "Never")
    private String answer;


}