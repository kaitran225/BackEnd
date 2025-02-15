package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentRequest;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.entity.Enum.StatusEnum;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.AppointmentMapper;
import com.healthy.backend.mapper.PsychologistsMapper;
import com.healthy.backend.mapper.StudentMapper;
import com.healthy.backend.repository.*;
import com.sun.jdi.request.InvalidRequestStateException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
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

    @Autowired
    TimeSlotRepository timeSlotRepository;



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
        // Kiểm tra và lấy thông tin time slot
        TimeSlots timeSlot = timeSlotRepository.findByIdWithPsychologist(request.getTimeSlotId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy time slot với ID: " + request.getTimeSlotId()));

        // Validate time slot
        if (timeSlot.getStatus() != TimeSlots.Status.Available) {
            throw new InvalidRequestStateException("Time slot không khả dụng");
        }
        if (!timeSlot.getPsychologist().getPsychologistID().equals(request.getPsychologistId())) {
            throw new InvalidRequestStateException("Time slot không thuộc về psychologist này");
        }

        // Kiểm tra student và psychologist
        Students student = studentRepository.findById(request.getStudentId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy student với ID: " + request.getStudentId()));
        Psychologists psychologist = psychologistRepository.findById(request.getPsychologistId())
                .orElseThrow(() -> new ResourceNotFoundException("Không tìm thấy psychologist với ID: " + request.getPsychologistId()));

        long currentCount = appointmentRepository.countTotalAppointments();

// Tạo appointmentID theo định dạng AP01, AP02,...
        String appointmentId = "AP" + String.format("%02d", currentCount + 1);

        // Tạo appointment mới
        Appointments appointment = new Appointments();
        appointment.setAppointmentID(appointmentId);
        appointment.setTimeSlotsID(timeSlot.getTimeSlotsID());
        appointment.setStudentID(student.getStudentID());
        appointment.setPsychologistID(psychologist.getPsychologistID());

        // Lưu appointment và cập nhật time slot
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



    public AppointmentResponse checkIn(String appointmentId) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        if (appointment.getStatus() != StatusEnum.Scheduled) {
            throw new InvalidRequestStateException("Appointment is not in Scheduled status");
        }

        appointment.setStatus(StatusEnum.InProgress);
        appointment.setCheckInTime(LocalDateTime.now());
        appointmentRepository.save(appointment);

        return buildAppointmentResponse(appointment);
    }

    public AppointmentResponse checkOut(String appointmentId) {
        Appointments appointment = appointmentRepository.findById(appointmentId)
                .orElseThrow(() -> new ResourceNotFoundException("Appointment not found with id: " + appointmentId));

        if (appointment.getStatus() != StatusEnum.InProgress) {
            throw new InvalidRequestStateException("Appointment is not in InProgress status");
        }

        appointment.setStatus(StatusEnum.Completed);
        appointment.setCheckOutTime(LocalDateTime.now());
        appointmentRepository.save(appointment);

        return buildAppointmentResponse(appointment);
    }



    private AppointmentResponse buildAppointmentResponse(Appointments appointment) {
        Students student = studentRepository.findById(appointment.getStudentID())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));
        Psychologists psychologist = psychologistRepository.findById(appointment.getPsychologistID())
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found"));

        return appointmentMapper.buildAppointmentResponse(
                appointment,
                psychologistMapper.buildPsychologistResponse(psychologist),
                studentMapper.buildStudentResponse(student)
        );
    }
}