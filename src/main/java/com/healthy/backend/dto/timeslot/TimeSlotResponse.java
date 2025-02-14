package com.healthy.backend.dto.timeslot;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
@Builder
public class TimeSlotResponse {
    private String timeSlotId;
    private LocalDate slotDate;
    private LocalTime startTime;
    private LocalTime endTime;
    private String status;
}
