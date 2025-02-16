package com.healthy.backend.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentUpdateRequest {

    @Schema(example = "TSPSY001160225")
    private String timeSlotId;
    @Schema(example = "Scheduled")
    private String status;
    @Schema(example = "Notes")
    private String notes;
}
