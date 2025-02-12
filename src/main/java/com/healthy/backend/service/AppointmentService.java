package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.AppointmentMapper;
import com.healthy.backend.mapper.PsychologistsMapper;
import com.healthy.backend.mapper.StudentMapper;
import com.healthy.backend.repository.AppointmentRepository;
import com.healthy.backend.repository.PsychologistRepository;
import com.healthy.backend.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;

    private final StudentRepository studentRepository;

    private final PsychologistRepository psychologistRepository;

    private final AppointmentMapper appointmentMapper;

    private final StudentMapper studentMapper;

    private final PsychologistsMapper psychologistMapper;

    public List<AppointmentResponse> getAllAppointments() {
        List<Appointments> appointments = appointmentRepository.findAll();
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        return appointments.stream()
                .map(appointment ->
                        appointmentMapper.buildAppointmentResponse(
                                appointment,
                                psychologistMapper.buildPsychologistResponse(
                                        Objects.requireNonNull(psychologistRepository.findById(
                                                appointment.getPsychologistID()).orElse(null))),
                                studentMapper.buildStudentResponse(
                                        Objects.requireNonNull(studentRepository.findById(
                                                appointment.getStudentID()).orElse(null)))
                        ))
                .collect(Collectors.toList());
    }

    public AppointmentResponse getAppointmentById(String id) {
        Appointments appointment = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No appointment by id" + id + "Not found"));
        return appointmentMapper.buildAppointmentResponse(
                appointment,  psychologistMapper.buildPsychologistResponse(
                        Objects.requireNonNull(psychologistRepository.findById(
                                appointment.getPsychologistID()).orElse(null))),
                studentMapper.buildStudentResponse(
                        Objects.requireNonNull(studentRepository.findById(
                                appointment.getStudentID()).orElse(null))));
    }
}