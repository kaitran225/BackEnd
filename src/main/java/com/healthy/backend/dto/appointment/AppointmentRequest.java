package com.healthy.backend.dto.appointment;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AppointmentRequest {

    @Schema(example = "PSY001")
    private String psychologistId;

    @Schema(example = "S001")
    private String studentId;

    @Schema(example = "TSPSY00127022500")
    private String timeSlotId;

    @Schema(hidden = true, example = "Active")
    private String status;

    @Schema(example = "Notes")
    private String note;
}
