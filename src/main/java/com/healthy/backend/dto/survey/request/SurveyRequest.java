package com.healthy.backend.dto.survey.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyRequest {
    @Valid
    @Schema(example = "Example Survey")
    private String title;
    @Valid
    @Schema(example = "Survey Description")
    private String description;
    @Valid
    @Schema(example = "GAD_7")
    private String standardType;
    @Schema(example = "2025-03-19")
    @Valid
    private String startDate;
    @Schema(example = "1")
    @Pattern(regexp = "^[1-9]$", message = "Periodic must be a number between 1 and 9")
    private Integer periodic;
    private List<QuestionRequest> question;
}
