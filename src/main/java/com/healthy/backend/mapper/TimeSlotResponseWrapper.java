package com.healthy.backend.mapper;

import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import lombok.*;

import java.util.List;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class TimeSlotResponseWrapper {
    private List<TimeSlotResponse> timeSlots;
    private String message;
}
