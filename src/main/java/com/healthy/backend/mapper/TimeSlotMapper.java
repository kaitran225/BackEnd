package com.healthy.backend.mapper;

import com.healthy.backend.dto.timeslot.TimeSlotResponse;
import com.healthy.backend.entity.TimeSlots;
import org.springframework.stereotype.Component;

@Component
public class TimeSlotMapper {
    public TimeSlotResponse toResponse(TimeSlots timeSlot) {
        return TimeSlotResponse.builder()
                .timeSlotId(timeSlot.getTimeSlotsID())
                .slotDate(timeSlot.getSlotDate())
                .startTime(timeSlot.getStartTime())
                .endTime(timeSlot.getEndTime())
                .status(timeSlot.getStatus().name())
                .build();
    }
}