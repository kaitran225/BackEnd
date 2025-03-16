package com.healthy.backend.dto.programs;

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
public class ProgramWeeklyScheduleResponse {
    @Schema(example = "Monday")
    private String weeklyAt;
    @Schema(example = "9:00 AM")
    private LocalTime startTime;
    @Schema(example = "10:00 AM")
    private LocalTime endTime;
}
