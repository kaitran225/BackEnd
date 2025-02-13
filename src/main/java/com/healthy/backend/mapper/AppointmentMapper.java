package com.healthy.backend.mapper;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.Appointments;
import org.springframework.stereotype.Component;

@Component
public class AppointmentMapper {

    public AppointmentResponse appointmentResponse(Appointments appointments){
        return AppointmentResponse.builder()
                .appointmentID(appointments.getAppointmentID())
                .CreatedAt(appointments.getCreatedAt())
                .Status(appointments.getStatus().name())
                .Text(appointments.getNotes())
                .timeSlotID(appointments.getTimeSlotsID())
                .UpdatedAt(appointments.getUpdatedAt())
                .build();
    }

    public AppointmentResponse appointmentResponse(Appointments appointments,
                                                   PsychologistResponse psychologistResponse,
                                                   StudentResponse studentResponse
    ){
        return AppointmentResponse.builder()
                .appointmentID(appointments.getAppointmentID())
                .CreatedAt(appointments.getCreatedAt())
                .Status(appointments.getStatus().name())
                .psychologistResponse(psychologistResponse)
                .studentResponse(studentResponse)
                .Text(appointments.getNotes())
                .timeSlotID(appointments.getTimeSlotsID())
                .UpdatedAt(appointments.getUpdatedAt())
                .build();
    }

    public AppointmentResponse buildAppointmentResponse(
            Appointments appointment,
            PsychologistResponse psychologistResponse,
            StudentResponse studentResponse
    ) {
        return AppointmentResponse.builder()
                .appointmentID(appointment.getAppointmentID())
                .CreatedAt(appointment.getCreatedAt())
                .Status(appointment.getStatus().name())
                .psychologistResponse(psychologistResponse)
                .studentResponse(studentResponse)
                .Text(appointment.getNotes())
                .timeSlotID(appointment.getTimeSlotsID())
                .UpdatedAt(appointment.getUpdatedAt())
                .build();
    }
}
