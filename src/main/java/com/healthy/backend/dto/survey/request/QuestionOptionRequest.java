package com.healthy.backend.dto.survey.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionOptionRequest {
    @Min(value = 0, message = "Value must be at least 0")
    @Max(value = 9, message = "Value must be at most 9")
    @Schema(example = "1")
    private int value;
    @Schema(example = "")
    private String label;
}
