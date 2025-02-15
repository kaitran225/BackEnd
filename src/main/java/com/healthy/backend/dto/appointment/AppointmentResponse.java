package com.healthy.backend.dto.appointment;

import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    @Schema(example = "APP001")
    private String appointmentID;

    @Schema(example = "TS150601")
    private String timeSlotID;

    @Schema(examples = "{" +
            "studentId='S001'," +
            "studentName='John Doe'," +
            "}"
    )
    private StudentResponse studentResponse;

    @Schema(examples = "{" +
            "psychologistId='PSY001'," +
            "specialization='Mental Specialist'," +
            "}"
    )
    private PsychologistResponse psychologistResponse;

    @Schema(example = "Active")
    private String Status;

    @Schema(example = "Notes")
    private String Text;

    @Schema(example = "2023-01-01")
    private LocalDateTime CreatedAt;

    @Schema(example = "2023-01-01")
    private LocalDateTime UpdatedAt;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}