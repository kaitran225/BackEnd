package com.healthy.backend.dto.survey.request;


import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionOptionRequest {
    @Schema(example = "")
    private int value;
    @Schema(example = "")
    private String label;
}
