package com.healthy.backend.dto.survey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestionResponse {
    private String questionId;
    private String questionText;
    private String questionCategory;
    private String questionOptions;
}
