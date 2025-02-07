package com.healthy.BackEnd.DTO.Student;

import com.healthy.BackEnd.DTO.Survey.SurveyResultsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentResponse {
    private String studentId;
    private String userId;
    private Integer grade;
    private String className;
    private String schoolName;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private Integer depressionScore;
    private Integer anxietyScore;
    private Integer stressScore;
    private List<SurveyResultsResponse> surveyResults;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

}