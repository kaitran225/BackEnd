package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.DTO.Appointment.AppointmentResponse;
import com.healthy.BackEnd.DTO.Psychologist.PsychologistResponse;
import com.healthy.BackEnd.DTO.Student.StudentResponse;
import com.healthy.BackEnd.Entity.Appointments;
import com.healthy.BackEnd.Entity.Psychologists;
import com.healthy.BackEnd.Entity.Students;
import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Repository.AppointmentRepository;
import com.healthy.BackEnd.Repository.PsychologistRepository;
import com.healthy.BackEnd.Repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
public class AppointmentService {

    private final AppointmentRepository appointmentRepository;
    private final StudentRepository studentRepository;
    private final PsychologistRepository psychologistRepository;

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