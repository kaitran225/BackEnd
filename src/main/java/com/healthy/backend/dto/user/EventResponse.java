package com.healthy.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import lombok.*;

import java.util.List;
import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventResponse {
    private List<Map<String, EventDetails>> event;
    @Setter
    @Getter
    public static class EventDetails {
        private List<AppointmentResponse> appointment;
        private List<ProgramsResponse> program;
        public EventDetails(List<AppointmentResponse> appointment, List<ProgramsResponse> program) {
            this.appointment = appointment;
            this.program = program;
        }

    }
}
