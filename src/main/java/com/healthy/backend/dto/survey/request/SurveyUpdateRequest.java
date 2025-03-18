package com.healthy.backend.dto.survey.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SurveyUpdateRequest {
    @Schema(example = "Example Survey")
    private String title;
    @Schema(example = "Survey Description")
    private String description;
    @Schema(example = "GAD_7")
    private String standType;
    @Schema(example = "ACTIVE")
    private String status;
    @Schema(example = "2025-01-01")
    private String startDate;
    @Schema(example = "1")
    private Integer periodic;
    @Schema(example = "")
    private List<QuestionUpdateRequest> question;
}
