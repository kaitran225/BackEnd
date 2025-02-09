package com.healthy.backend.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserDTO {
    private String userId;
    private String username;
    private String passwordHash;
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