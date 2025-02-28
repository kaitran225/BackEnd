package com.healthy.backend.dto.appointment;

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

    private LocalDateTime appointmentDate;
    private String studentName;
    private String feedback;
    private int rating;
}
