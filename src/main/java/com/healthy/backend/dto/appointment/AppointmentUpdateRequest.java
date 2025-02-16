package com.healthy.backend.dto.appointment;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AppointmentUpdateRequest {
    private String timeSlotId;
    private String notes;
}
