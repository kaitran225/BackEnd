package com.healthy.backend.dto.psychologist;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class LeaveRequest {

    @Schema(example = "PSY001")
    @NotBlank(message = "Psychologist ID is required")
    private String psychologistId;

    @Schema(example = "2025-03-01")
    @NotNull(message = "Start date is required")
    @FutureOrPresent(message = "Start date must be in present or future")
    private LocalDate startDate;

    @Schema(example = "2025-12-31")
    @NotNull(message = "End date is required")
    @FutureOrPresent(message = "End date must be in present or future")
    private LocalDate endDate;

    @Schema(example = "Reason")
    @NotBlank(message = "Reason is required")
    private String reason;
}