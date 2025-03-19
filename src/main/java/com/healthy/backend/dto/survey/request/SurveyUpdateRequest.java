package com.healthy.backend.dto.survey.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
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
    @Min(value = 1, message = "Periodic must be at least 1")
    @Max(value = 8, message = "Periodic must be at most 8")
    @Schema(example = "1")
    private int periodic;

}
