package com.healthy.backend.dto.programs;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProgramWeeklyScheduleRequest {
    @Schema(example = "Monday")
    private String weeklyAt;
    @Schema(example = "9:00 AM")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:mm a")
    private String startTime;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "h:mm a")
    @Schema(example = "10:00 AM")
    private String endTime;
}
