package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.Students;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.repository.AppointmentRepository;
import com.healthy.backend.repository.PsychologistRepository;
import com.healthy.backend.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AppointmentService {
    @Autowired
    AppointmentRepository appointmentRepository;
    @Autowired
    StudentRepository studentRepository;
    @Autowired
    PsychologistRepository psychologistRepository;

    public AppointmentResponse covertAppointmentDTO(Appointments appointments) {

        Students student = studentRepository.findById(appointments.getStudentID())
                .orElseThrow(() -> new ResourceNotFoundException("No student found with id" + appointments.getStudentID()));
        Psychologists psychologist = psychologistRepository.findById(appointments.getPsychologistID())
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id" + appointments.getPsychologistID()));

        return AppointmentResponse.builder()
                .appointmentID(appointments.getAppointmentID())
                .CreatedAt(appointments.getCreatedAt())
                .MeetingLink(appointments.getMeetingLink())
                .Status(appointments.getStatus().name())
                .psychologistResponse(
                        PsychologistResponse.builder()
                                .psychologistId(psychologist.getPsychologistID())
                                .status(psychologist.getStatus().name())
                                .specialization(psychologist.getSpecialization())
                                .yearsOfExperience(psychologist.getYearsOfExperience())
                                .build()
                )
                .studentResponse(StudentResponse.builder()
                        .studentId(student.getStudentID())
                        .grade(student.getGrade())
                        .className(student.getClassName())
                        .schoolName(student.getSchoolName())
                        .depressionScore(student.getDepressionScore())
                        .anxietyScore(student.getAnxietyScore())
                        .stressScore(student.getStressScore())
                        .build())
                .Text(appointments.getNotes())
                .timeSlotID(appointments.getTimeSlotsID())
                .UpdatedAt(appointments.getUpdatedAt())
                .build();
    }

    public List<AppointmentResponse> getAllAppointmentDTO() {
        List<Appointments> appointments = appointmentRepository.findAll();
        return
                appointments.stream()
                        .map(this::covertAppointmentDTO)
                        .collect(Collectors.toList());
    }

    public AppointmentResponse getAppointmentById(String id) {
        Appointments appointments = appointmentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No appointment by id" + id + "Not found"));
        return covertAppointmentDTO(appointments);
    }
}