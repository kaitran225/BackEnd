package com.healthy.BackEnd.DTO.Appointment;

import com.healthy.BackEnd.DTO.Psychologist.PsychologistResponse;
import com.healthy.BackEnd.DTO.Student.StudentResponse;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppointmentResponse {

    private String appointmentID;
    private String timeSlotID;
    private StudentResponse studentResponse;
    private PsychologistResponse psychologistResponse;
    private String Status;
    private String Text;
    private String MeetingLink;
    private String AppointmentType;
    private LocalDateTime CreatedAt;
    private LocalDateTime UpdatedAt;

}