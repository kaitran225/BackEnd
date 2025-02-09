package com.healthy.backend.dto.psychologist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.user.UsersResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PsychologistResponse {
    private String psychologistId;
    private String specialization;
    private Integer yearsOfExperience;
    private String status;
    private UsersResponse usersResponse;
    private List<AppointmentResponse> appointment;
}
