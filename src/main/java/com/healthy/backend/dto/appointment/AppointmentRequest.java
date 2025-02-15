package com.healthy.backend.dto.appointment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentRequest {

    @NotBlank(message = "Student ID is required")
    private String studentId; // ID của học sinh đặt lịch hẹn


    @NotBlank(message = "Time slot ID is required")
    private String timeSlotId; // ID của time slot được chọn

    private String notes; // Ghi chú (nếu có)

}