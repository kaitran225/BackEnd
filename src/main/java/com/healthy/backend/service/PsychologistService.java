package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistRequest;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.Appointments;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.PsychologistsMapper;
import com.healthy.backend.repository.AppointmentRepository;
import com.healthy.backend.repository.PsychologistRepository;
import com.healthy.backend.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PsychologistService {

    @Autowired
    public PsychologistRepository psychologistRepository;

    @Autowired
    public AppointmentRepository appointmentRepository;

    @Autowired
    public UserRepository userRepository;

    @Autowired
    public PsychologistsMapper psychologistsMapper;

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

    public PsychologistResponse updatePsychologist(String id, PsychologistRequest request) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id " + id));

        // Map thông tin từ request vào psychologist
        psychologistsMapper.mapToEntity(request);

        // Lưu thông tin cập nhật vào cơ sở dữ liệu
        psychologistRepository.save(psychologist);

        // Trả về thông tin psychologist đã cập nhật
        return convert(psychologist);
    }
}
