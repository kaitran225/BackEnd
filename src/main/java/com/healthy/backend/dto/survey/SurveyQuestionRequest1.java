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
public class SurveyQuestionRequest1 {
    @Schema(example = "DEPRESSION")
    private String category;
    @Schema(example = "")
    private List<QuestionResponse1> questionList;

}
