package com.healthy.backend.mapper;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Users;
import java.util.List;

public class AppointmentMapper {
    public List<AppointmentResponse> buildAppointmentResponseList(
            Users user,
            List<Appointments> appointmentsList,
            PsychologistResponse psychologistResponse,
            StudentResponse studentResponse
    ){
        return appointmentsList.stream()
                .map(appointments -> {

                    return this.buildAppointmentResponse(
                            user,
                            appointments,
                            psychologistResponse,
                            studentResponse
                            );
                })
                .toList();
    }
    public AppointmentResponse buildAppointmentResponse(
            Users user,
            Appointments appointment,
            PsychologistResponse psychologistResponse,
            StudentResponse studentResponse
    ) {
        if(user.getRole() == Users.UserRole.PSYCHOLOGIST) {
            psychologistResponse = null;
        }
        if(user.getRole() == Users.UserRole.STUDENT){
            studentResponse = null;
        }
        return AppointmentResponse.builder()
                .appointmentID(appointment.getAppointmentID())
                .CreatedAt(appointment.getCreatedAt())
                .MeetingLink(appointment.getMeetingLink())
                .Status(appointment.getStatus().name())
                .psychologistResponse(psychologistResponse)
                .studentResponse(studentResponse)
                .Text(appointment.getNotes())
                .timeSlotID(appointment.getTimeSlotsID())
                .UpdatedAt(appointment.getUpdatedAt())
                .build();
    }
}
