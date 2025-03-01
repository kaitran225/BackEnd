package com.healthy.backend.dto.survey;


import com.fasterxml.jackson.annotation.JsonInclude;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder


@JsonInclude(JsonInclude.Include.NON_NULL)
public class QuestionOption {
    @Schema(example = "")
    private int value;
    @Schema(example = "")
    private String label;
    @Schema(example = "")
    private String answerID;
    
    
}
