package com.healthy.backend.dto.timeslot;

import com.healthy.backend.dto.psychologist.PsychologistResponse;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PsychologistAvailabilityResponse {
    @Schema(example = "PSY001")
    private PsychologistResponse psychologist;
    @Schema(example = "2023-01-01")
    private List<TimeSlotResponse> availableTimeSlots;

}
