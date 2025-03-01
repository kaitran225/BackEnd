package com.healthy.backend.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentFeedbackResponse {

    @Schema(example = "2025-01-01")
    private LocalDateTime appointmentDate;
    @Schema(example = "Student Name")
    private String studentName;
    @Schema(example = "Student Feedback")
    private String feedback;
    @Schema(example = "5")
    private int rating;
}
