package com.healthy.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class StudentDTO {
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer depressionScore;
    private Integer anxietyScore;
    private Integer stressScore;
}