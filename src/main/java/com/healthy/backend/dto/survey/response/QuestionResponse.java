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
public class QuestionResponse {
    @Schema(example = "SQR001")
    private String id;
    @Schema(example = "How are you feeling today?")
    private String questionText;
    private List<QuestionOption> questionOptions;
}