package com.healthy.BackEnd.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import java.time.LocalDateTime;
import java.util.List;

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
    private List<StudentDTO> children;
    private PsychologistDTO psychologistInfo;
    private StudentDTO studentInfo;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}