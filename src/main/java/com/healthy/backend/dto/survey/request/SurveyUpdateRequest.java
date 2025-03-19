package com.healthy.backend.dto.survey.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Pattern;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyUpdateRequest {
    @Valid
    @Schema(example = "Example Survey")
    private String title;
    @Valid
    @Schema(example = "Survey Description")
    private String description;
    @Valid
    @Schema(example = "GAD_7")
    private String standType;
    @Valid
    @Schema(example = "ACTIVE")
    private String status;
    @Valid
    @Schema(example = "2025-01-01")
    private String startDate;
    @Pattern(regexp = "^[1-9]$", message = "Periodic must be a number between 1 and 9")
    @Schema(example = "1")
    private int periodic;
}
