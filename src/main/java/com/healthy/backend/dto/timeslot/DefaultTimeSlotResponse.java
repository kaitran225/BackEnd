package com.healthy.backend.dto.timeslot;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalTime;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class DefaultTimeSlotResponse {
    @Schema(example = "")
    private String slotId;
    @Schema(example = "")
    private LocalTime startTime;
    @Schema(example = "")
    private LocalTime endTime;
    @Schema(example = "")
    private String period;
}
