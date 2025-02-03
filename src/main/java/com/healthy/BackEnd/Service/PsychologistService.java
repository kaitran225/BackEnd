package com.healthy.BackEnd.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import com.healthy.BackEnd.DTO.Psychologist.PsychologistResponse;
import com.healthy.BackEnd.Entity.Psychologists;
import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Repository.PsychologistRepository;
import com.healthy.BackEnd.DTO.Appointment.AppointmentResponse;
import com.healthy.BackEnd.DTO.User.UsersResponse;
import com.healthy.BackEnd.Entity.Appointments;
import com.healthy.BackEnd.Entity.Users;
import com.healthy.BackEnd.Repository.AppointmentRepository;
import com.healthy.BackEnd.Repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PsychologistService {

    @Autowired
    public PsychologistRepository psychologistRepository;

    @Autowired
    public AppointmentRepository appointmentRepository;

    @Autowired
    public UserRepository userRepository;

    public PsychologistResponse convertToDTO(Psychologists psychologist) {
        return PsychologistResponse.builder()
                .psychologistId(psychologist.getPsychologistID())
                .status(psychologist.getStatus().name())
                .specialization(psychologist.getSpecialization())
                .yearsOfExperience(psychologist.getYearsOfExperience())
                .userId(psychologist.getUserID())
                .build();

    }

    public List<PsychologistResponse> getAllPsychologistDTO() {
        List<Psychologists> psychologists = psychologistRepository.findAll();

        return psychologists.stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public PsychologistResponse getPsychologistById(String id) {
        Psychologists psychologist = psychologistRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id" + id));

        return convertToDTO(psychologist);
    }

    public List<AppointmentResponse> appointmentDTO(Psychologists psychologists) {
        AppointmentService appointmentService = new AppointmentService();
        List<Appointments> appointments = appointmentRepository.findByPsychologistID(psychologists.getPsychologistID());
        if (!appointments.isEmpty()) {
            return appointments.stream()
                    .map(appointmentService::covertChildAppointmentDTO)
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();

    }

    public PsychologistResponse convert(Psychologists psychologist) {
        Users users = userRepository.findById(psychologist.getUserID())
                .orElseThrow(() -> new ResourceNotFoundException("No user found with psychologistID"));
        return
                PsychologistResponse.builder()
                        .psychologistId(psychologist.getPsychologistID())
                        .status(psychologist.getStatus().name())
                        .specialization(psychologist.getSpecialization())
                        .yearsOfExperience(psychologist.getYearsOfExperience())
                        .userId(psychologist.getUserID())
                        .usersResponse(UsersResponse.builder()
                                .fullName(users.getFullName())
                                .username(users.getUsername())
                                .phoneNumber(users.getPhoneNumber())
                                .email(users.getEmail())
                                .gender(users.getGender().toString())
                                .build())
                        .appointment(appointmentDTO(psychologist))
                        .build();
    }
}
