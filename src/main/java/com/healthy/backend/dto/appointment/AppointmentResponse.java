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
    @NotBlank(message = "Appointment ID is required")
    private String appointmentID;

    private String timeSlotID;
    private StudentResponse studentResponse;
    private PsychologistResponse psychologistResponse;
    private String Status;
    private String Text;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;
    private LocalDateTime checkInTime;
    private LocalDateTime checkOutTime;
}