package com.healthy.backend.dto.survey.request;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionRequest {
    @Schema(example = "SQR001")
    private String id;
    @Schema(example = "How are you feeling today?")
    private String questionText;
    private List<QuestionOptionRequest> questionOptions;
}
