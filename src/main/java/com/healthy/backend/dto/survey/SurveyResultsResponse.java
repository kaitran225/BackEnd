package com.healthy.backend.dto.survey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveyResultsResponse {
    private String surveyId;
    private String surveyName;
    private String description;
    private List<SurveyQuestionResultResponse> questions;
}
