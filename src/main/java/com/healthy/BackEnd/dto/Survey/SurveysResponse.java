package com.healthy.BackEnd.DTO.Survey;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SurveysResponse {
    private String surveyId;
    private String surveyName;
    private String description;
    private String category;
    private String status;
    private String createdAt;
    private String createBy;
}
