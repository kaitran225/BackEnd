package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentRequest;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.entity.Enum.StatusEnum;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.AppointmentMapper;
import com.healthy.backend.mapper.PsychologistsMapper;
import com.healthy.backend.mapper.StudentMapper;
import com.healthy.backend.repository.*;
import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
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

    private final TimeSlotRepository timeSlotRepository;

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


    public AppointmentResponse bookAppointment(AppointmentRequest request) {
        TimeSlots timeSlot = timeSlotRepository.findByIdWithPsychologist(request.getTimeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Timeslot not found with id: " + request.getTimeSlotId()));

        // Validate time slot
        if (timeSlot.getStatus() != TimeSlots.Status.Available) {
            throw new InvalidRequestStateException("Time slot is not valid");
        }
        if (!timeSlot.getPsychologist().getPsychologistID().equals(request.getPsychologistId())) {
            throw new InvalidRequestStateException("Time slot is not available");
        }

        // Validate student and psychologist
        Students student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found with ID: " + request.getStudentId()));
        Psychologists psychologist = psychologistRepository.findById(request.getPsychologistId())
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found with ID: " + request.getPsychologistId()));

        Appointments appointment = new Appointments();
        appointment.setAppointmentID(generateAppointmentId());
        appointment.setTimeSlotsID(timeSlot.getTimeSlotsID());
        appointment.setStudentID(student.getStudentID());
        appointment.setPsychologistID(psychologist.getPsychologistID());

        Appointments savedAppointment = appointmentRepository.save(appointment);
        timeSlot.setStatus(TimeSlots.Status.Booked);
        timeSlotRepository.save(timeSlot);

        // Map sang DTO
        return appointmentMapper.buildAppointmentResponse(
                savedAppointment,
                psychologistMapper.buildPsychologistResponse(psychologist),
                studentMapper.buildStudentResponse(student)
        );
    }
    // Cancel
    public boolean cancelAppointment(String appointmentId) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        if (appointment.getStatus() == StatusEnum.InProgress) {
            throw new InvalidRequestStateException("Can not cancel an appointment that is In Progress");
        }
        if (appointment.getStatus() == StatusEnum.Completed) {
            throw new InvalidRequestStateException("Appointment is already completed");
        }
        appointment.setStatus(StatusEnum.Cancelled);
        appointmentRepository.save(appointment);
        return true;
    }

    // Check in
    public AppointmentResponse checkIn(String appointmentId) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        if (appointment.getStatus() == StatusEnum.Cancelled) {
            throw new OperationFailedException("Appointment is cancelled");
        }

        if (appointment.getStatus() == StatusEnum.Completed) {
            throw new OperationFailedException("Appointment is already completed");
        }

        if (appointment.getStatus() == StatusEnum.InProgress) {
            throw new OperationFailedException("Appointment is already in progress");
        }


        appointment.setStatus(StatusEnum.InProgress);
        appointment.setCheckInTime(LocalDateTime.now());
        appointmentRepository.save(appointment);

        return appointmentMapper.buildAppointmentResponse(
                appointment,
                psychologistMapper.buildPsychologistResponse(
                        Objects.requireNonNull(psychologistRepository.findById(
                                appointment.getPsychologistID()).orElse(null))),
                studentMapper.buildStudentResponse(
                        Objects.requireNonNull(studentRepository.findById(
                                appointment.getStudentID()).orElse(null)))
        );
    }

    // Check out
    public AppointmentResponse checkOut(String appointmentId) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        if (appointment.getStatus() == StatusEnum.Cancelled) {
            throw new OperationFailedException("Appointment is cancelled");
        }

        if (appointment.getStatus() == StatusEnum.Completed) {
            throw new OperationFailedException("Appointment is already completed");
        }

        if (appointment.getStatus() == StatusEnum.Scheduled) {
            throw new OperationFailedException("You have not checked in yet");
        }

        appointment.setStatus(StatusEnum.Completed);
        appointment.setCheckOutTime(LocalDateTime.now());
        appointmentRepository.save(appointment);

        return appointmentMapper.buildAppointmentResponse(
                appointment,
                psychologistMapper.buildPsychologistResponse(
                        Objects.requireNonNull(psychologistRepository.findById(
                                appointment.getPsychologistID()).orElse(null))),
                studentMapper.buildStudentResponse(
                        Objects.requireNonNull(studentRepository.findById(
                                appointment.getStudentID()).orElse(null)))
        );
    }

    // Generate appointment id
    private String generateAppointmentId() {
        String lastCode = appointmentRepository.findLastAppointmentId();
        if (lastCode == null || lastCode.length() < 3) { // APP
            throw new IllegalArgumentException("Invalid last appointment code");
        }
        String prefix = lastCode.substring(0, 3);
        int number = Integer.parseInt(lastCode.substring(3));
        return prefix + String.format("%03d", number + 1);
    }
}