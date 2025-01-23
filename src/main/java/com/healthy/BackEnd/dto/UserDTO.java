package com.healthy.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private String userId;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private String gender;
    private String role;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private Integer depressionScore;
    private Integer anxietyScore;
    private Integer stressScore; // For student
    private String specialization;  // For trainers/experts
    private Integer yearsOfExperience;  // For trainers/experts
} 