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
public class QuestionUpdateRequest {
    private String questionId;
    @Schema(example = "How are you feeling today?")
    private String questionText;
    private List<QuestionOptionUpdateRequest> questionOptions;
}
