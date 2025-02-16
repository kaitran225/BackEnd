package com.healthy.backend.dto.survey;

import lombok.Data;

@Data
public class AnswerRequest {
    private String questionId;
    private String answerId;
}
