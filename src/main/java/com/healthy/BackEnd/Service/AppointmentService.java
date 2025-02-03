package com.healthy.BackEnd.Service;
import java.util.List;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import com.healthy.BackEnd.DTO.Appointment.AppointmentResponse;
import com.healthy.BackEnd.Entity.Appointments;

import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Repository.AppointmentRepository;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class AppointmentService  {
    @Autowired
    AppointmentRepository appointmentRepository;
    public AppointmentResponse covertAppointmentDTO(Appointments appointments) {
         
        return AppointmentResponse.builder()
            .appointmentID(appointments.getAppointmentID())
            .CreatedAt(appointments.getCreatedAt())
            .MeetingLink(appointments.getMeetingLink())
            .PsychologistID(appointments.getPsychologistID())
            .Status(appointments.getStatus().name())
            .StudentID(appointments.getStudentID())
            .Text(appointments.getNotes())
            .timeSlotID(appointments.getTimeSlotsID())
            .UpdatedAt(appointments.getUpdatedAt())
            .build();
    }

    public AppointmentResponse covertChildAppointmentDTO(Appointments appointments) {
        return AppointmentResponse.builder()
            .appointmentID(appointments.getAppointmentID())
            
            .timeSlotID(appointments.getTimeSlotsID())
            .MeetingLink(appointments.getMeetingLink())
            .StudentID(appointments.getStudentID())
            .build();
    }

    public List<AppointmentResponse> getAllAppointmentDTO () {
        List<Appointments> appointments = appointmentRepository.findAll();
        return 
            appointments.stream()
            .map(this :: covertAppointmentDTO)
            .collect(Collectors.toList());
        
    }

    public AppointmentResponse getAppointmentById(String id) {
        Appointments appointments = appointmentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No appointment by id" + id + "Not found"));
        return covertAppointmentDTO(appointments);
    }
}