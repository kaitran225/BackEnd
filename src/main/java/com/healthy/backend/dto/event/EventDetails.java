package com.healthy.backend.dto.event;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventDetails {
    private String childrenName;
    private List<AppointmentResponse> appointment;
    private List<ProgramsResponse> program;

    public EventDetails(List<AppointmentResponse> appointment, List<ProgramsResponse> program) {
        this.appointment = appointment;
        this.program = program;
    }
}