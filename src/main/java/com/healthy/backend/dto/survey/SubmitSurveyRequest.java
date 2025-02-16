package com.healthy.backend.dto.survey;

import lombok.Data;

import java.util.List;

@Data
public class SubmitSurveyRequest {
    private String studentId;
    private List<AnswerRequest> answers;
}
