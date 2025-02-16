package com.healthy.backend.dto.timeslot;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class TimeSlotResponse {
    @Schema(example = "TSPSY00127022500")
    private String timeSlotId;
    @Schema(example = "2023-01-01")
    private LocalDate slotDate;
    @Schema(example = "2023-01-01 00:00:00")
    private LocalTime startTime;
    @Schema(example = "2023-01-01 00:00:00")
    private LocalTime endTime;
    @Schema(example = "Available")
    private String status;
}
