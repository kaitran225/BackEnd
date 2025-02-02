package com.healthy.BackEnd.DTO;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonInclude;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import  lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AppointmentDTO {

    @NotNull(message= "AppointmentID is required")
    private String appointmentID;

    @Size(max= 36, message= "TimeSlotsID can be at most 36 character")
    private String timeSlotID;

    @NotNull(message= "StudentID is required")
    @Size(max= 36, message= "StudentID can be at most 36 character")
    private String StudentID;

    @NotNull(message="PsychologistID is required")
    @Size(max= 36, message= "PsychologistID can be at most 36 character")
    private String PsychologistID;

    @NotNull(message= "Status is required")
    private String  Status;

    private String Text;

    @Size(max =255, message = "MeetingLink can be at most 255 character")
    private String MeetingLink;

    @NotNull(message= "AppointmentType is required")
    private String AppointmentType;

    private LocalDateTime CreatedAt;

    private LocalDateTime UpdatedAt;

}