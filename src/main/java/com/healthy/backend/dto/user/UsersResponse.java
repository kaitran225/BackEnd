package com.healthy.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.entity.Users;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "US001")
    private String userId;
    @Schema(example = "username1")
    private String username;
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9" +
            ".eyJzdWIiOiJhZG1pbiIsImlhdCI6MTczOTUzNTI2NiwiZXhwIjoxNzM5NjIxNjY2fQ" +
            ".WWZ2yAdX80F6H1HZuqRGrre4C7YBmarUdQFyXbxKpLc")
    private String passwordHash;
    @Schema(example = "John Doe")
    private String fullName;
    @Schema(example = "user@example.com")
    private String email;
    @Schema(example = "1234567890")
    private String phoneNumber;
    @Schema(example = "Example Address")
    private String address;
    @Schema(example = "Male")
    private String gender;
    @Schema(example = "Psychologist")
    private String role;
    @Schema(example = "Psychologist{" +
            "psychologistId='PSY001'," +
            "specialization='Mental Specialist'," +
            "}")
    private PsychologistResponse psychologistInfo;
    @Schema(example = "Student{" +
            "studentId='S001'," +
            "grade=10," +
            "className='A'," +
            "schoolName='Example School'," +
            "}")
    private StudentResponse studentInfo;
    @Schema(examples = {"Student 1", "Student 2", "Student 3"})
    private List<StudentResponse> children;
    @Schema(examples = {"Appointment 1", "Appointment 2", "Appointment 3"})
    private List<AppointmentResponse> appointmentsRecord;
    @Schema(examples = {"Survey 1", "Survey 2", "Survey 3"})
    private List<SurveyResultsResponse> surveyResults;
    @Schema(example = "2023-01-01")
    private LocalDateTime createdAt;
    @Schema(example = "2023-01-01")
    private LocalDateTime updatedAt;
}