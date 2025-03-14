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
public class QuestionResponse {
    @Schema(example = "SQR001")
    private String id;
    @Schema(example = "How are you feeling today?")
    private String questionText;
    @Schema(example = "General")
    private String questionCategory;
    private List<QuestionOption> questionOptions;
}
