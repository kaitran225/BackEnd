package com.healthy.BackEnd.DTO.Survey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyQuestionResultResponse {
    private String questionId;
    private String categoryName;
    private String questionText;
    private String resultId;
    private String answerId;
    private String answer;
    private Integer score;
} 