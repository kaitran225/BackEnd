package com.healthy.BackEnd.Service;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;

import com.healthy.BackEnd.dto.AppointmentDTO;
import com.healthy.BackEnd.entity.Appointments;

import com.healthy.BackEnd.exception.ResourceNotFoundException;
import com.healthy.BackEnd.repository.AppointmentRepository;






public class AppointmentService  {
    @Autowired
    AppointmentRepository appointmentRepository;



    public AppointmentDTO coverAppointmentDTO(Appointments appointments) {
         
        return AppointmentDTO.builder()
            .appointmentID(appointments.getAppointmentID())
            .AppointmentType(appointments.getAppointmentType().name())
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

    public List<AppointmentDTO> getAllAppointmentDTO () {
        List<Appointments> appointments = appointmentRepository.findAll();
        return 
            appointments.stream()
            .map(this :: coverAppointmentDTO)
            .collect(Collectors.toList());
        
    }

    public AppointmentDTO getAppointmentById(String id) {
        Appointments appointments = appointmentRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No appointment by id" + id + "Not found"));
        return coverAppointmentDTO(appointments);
    }
}