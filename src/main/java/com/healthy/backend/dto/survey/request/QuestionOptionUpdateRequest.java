package com.healthy.backend.dto.survey.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionOptionUpdateRequest {
    @Schema(example = "")
    @Pattern(regexp = "^[1-9]$", message = "Value must be a number between 1 and 9")
    private int value;
    @Schema(example = "")
    private String label;
    @Schema(example = "")
    private String optionID;
}
