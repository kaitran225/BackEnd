package com.healthy.backend.dto.survey.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyRequest {
    @Schema(example = "Example Survey")
    private String title;
    @Schema(example = "Survey Description")
    private String description;
    @Schema(example = "GAD_7")
    private String standType;
    @Schema(example = "2025-01-01")
    private String startDate;
    @Schema(example = "1")
    private Integer periodic;
    @Schema(example = "UID002")
    private List<QuestionRequest> question;
}
