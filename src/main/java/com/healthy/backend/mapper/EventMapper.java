package com.healthy.backend.mapper;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.dto.user.EventResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Programs;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class EventMapper {

    public EventResponse buildEventResponse(
            List<Appointments> appointments,
            List<Programs> programs
    ) {
        return EventResponse.builder()
                .build();
    }
}
