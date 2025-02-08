package com.healthy.BackEnd.DTO.Psychologist;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.healthy.BackEnd.DTO.Appointment.AppointmentResponse;
import com.healthy.BackEnd.DTO.User.UsersResponse;
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
