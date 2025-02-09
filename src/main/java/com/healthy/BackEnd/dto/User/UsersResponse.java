package com.healthy.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.entity.Users;
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
public class UsersResponse {
    private String userId;
    private String username;
    private String passwordHash;
    private String fullName;
    private String email;
    private String phoneNumber;
    private String address;
    private String gender;
    private String role;
    private PsychologistResponse psychologistInfo;
    private StudentResponse studentInfo;
    private List<StudentResponse> children;
    private List<AppointmentResponse> appointmentsRecord;
    private List<SurveyResultsResponse> surveyResults;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    public Users toUser() {
        return Users.builder()
                .userId(userId)
                .username(username)
                .passwordHash(passwordHash)
                .fullName(fullName)
                .email(email)
                .phoneNumber(phoneNumber)
                .role(Users.UserRole.valueOf(role))
                .gender(Users.Gender.valueOf(gender))
                .build();
    }
}