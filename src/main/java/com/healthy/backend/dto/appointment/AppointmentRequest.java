package com.healthy.backend.dto.appointment;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentRequest {

    @Schema(hidden = true)
    private String userId;

    private String timeSlotId;

    @Schema(hidden = true, example = "Active")
    private String status;

    @Schema(example = "Notes")
    private String note;
}
