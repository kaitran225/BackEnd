package com.healthy.backend.dto.timeslot;

import com.healthy.backend.dto.psychologist.PsychologistResponse;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class PsychologistAvailabilityResponse {

    private PsychologistResponse psychologist;
    private List<TimeSlotResponse> availableTimeSlots;

}
