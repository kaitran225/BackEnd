package com.healthy.backend.dto.psychologist;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder
public class LeaveRequestDTO {

    @NotBlank(message = "Psychologist ID is required")

    private String psychologistId;

    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in present or future")
    private LocalDate startDate;
    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be in present or future")
    private LocalDate endDate;

    @NotBlank(message = "Reason is required")

    private String reason;
}