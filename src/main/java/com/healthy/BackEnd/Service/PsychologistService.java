package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.DTO.Appointment.AppointmentResponse;
import com.healthy.BackEnd.DTO.Psychologist.PsychologistResponse;
import com.healthy.BackEnd.DTO.Student.StudentResponse;
import com.healthy.BackEnd.DTO.User.UsersResponse;
import com.healthy.BackEnd.Entity.Appointments;
import com.healthy.BackEnd.Entity.Psychologists;
import com.healthy.BackEnd.Entity.Users;
import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Repository.AppointmentRepository;
import com.healthy.BackEnd.Repository.PsychologistRepository;
import com.healthy.BackEnd.Repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PsychologistService {

    public final PsychologistRepository psychologistRepository;

    public final AppointmentRepository appointmentRepository;

    public final UserRepository userRepository;

    public List<PsychologistResponse> getAllPsychologistDTO() {
        List<Psychologists> psychologists = psychologistRepository.findAll();

        return psychologists.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public PsychologistResponse getPsychologistById(String id) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id" + id));
        return convert(psychologist);
    }

    public PsychologistResponse convert(Psychologists psychologist) {
        List<Appointments> appointments = appointmentRepository.findByPsychologistID(psychologist.getPsychologistID());
        Users users = userRepository.findById(psychologist.getUserID())
                .orElseThrow(() -> new ResourceNotFoundException("No user found with psychologistID"));
        return
                PsychologistResponse.builder()
                        .psychologistId(psychologist.getPsychologistID())
                        .status(psychologist.getStatus().name())
                        .specialization(psychologist.getSpecialization())
                        .yearsOfExperience(psychologist.getYearsOfExperience())
                        .usersResponse(UsersResponse.builder()
                                .fullName(users.getFullName())
                                .username(users.getUsername())
                                .phoneNumber(users.getPhoneNumber())
                                .email(users.getEmail())
                                .gender(users.getGender().toString())
                                .build())
                        .appointment(
                                appointments.isEmpty()
                                        ? Collections.emptyList() : appointments.stream()
                                        .map(a -> AppointmentResponse.builder()
                                                .appointmentID(a.getAppointmentID())
                                                .CreatedAt(a.getCreatedAt())
                                                .MeetingLink(a.getMeetingLink())
                                                .Status(a.getStatus().name())
                                                .studentResponse(
                                                        StudentResponse.builder()
                                                                .studentId(a.getStudentID())
                                                                .grade(a.getStudent().getGrade())
                                                                .className(a.getStudent().getClassName())
                                                                .schoolName(a.getStudent().getSchoolName())
                                                                .depressionScore(a.getStudent().getDepressionScore())
                                                                .anxietyScore(a.getStudent().getAnxietyScore())
                                                                .stressScore(a.getStudent().getStressScore())
                                                                .build()
                                                )
                                                .Text(a.getNotes())
                                                .timeSlotID(a.getTimeSlotsID())
                                                .UpdatedAt(a.getUpdatedAt()).build()
                                        )
                                        .collect(Collectors.toList())
                        )
                        .build();
    }
}
