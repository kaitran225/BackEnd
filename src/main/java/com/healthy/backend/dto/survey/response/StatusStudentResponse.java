package com.healthy.backend.dto.survey.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StatusStudentResponse {
    @Schema(example = "")
    private String studentId;
    @Schema(example = "")
    private String score;
    @Schema(example = "")
    private String status;
    @Schema(example = "")
    private String resultStd;
    @Schema(example = "")
    private Integer valueOfQuestion;
    @Schema(example = "")
    private String completeStatus;
    @Schema(example = "")
    private String studentComplete;
    @Schema(example = "")
    private LocalDateTime lastCompleteDate;
}