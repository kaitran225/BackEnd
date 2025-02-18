package com.healthy.backend.dto.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDetails {
    private List<AppointmentResponse> appointment;
    private List<ProgramsResponse> program;
}