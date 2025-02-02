package com.healthy.BackEnd.Service;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;

import lombok.RequiredArgsConstructor;

import com.healthy.BackEnd.dto.PsychologistDTO;
import com.healthy.BackEnd.entity.Psychologists;
import com.healthy.BackEnd.exception.ResourceNotFoundException;
import com.healthy.BackEnd.repository.PsychologistRepository;

import org.springframework.stereotype.Service;

import com.healthy.BackEnd.dto.AppointmentDTO;
import com.healthy.BackEnd.dto.UserDTO;
import com.healthy.BackEnd.entity.Appointments;
import com.healthy.BackEnd.entity.Users;
import com.healthy.BackEnd.repository.AppointmentRepository;
import com.healthy.BackEnd.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class PsychologistService {
    
    @Autowired
    public PsychologistRepository psychologistRepository;

    @Autowired
    public AppointmentRepository appointmentRepository;

    @Autowired
    public UserRepository userRepository;

    

    

    public PsychologistDTO convertToDTO(Psychologists psychologist) {
        return PsychologistDTO.builder()
                .psychologistId(psychologist.getPsychologistID())
                .status(psychologist.getStatus().name())
                .specialization(psychologist.getSpecialization())
                .yearsOfExperience(psychologist.getYearsOfExperience())
                .userId(psychologist.getUserID())
                .build();
                
    }



    public List<PsychologistDTO> getAllPsychologistDTO() {
        List<Psychologists> psychologists = psychologistRepository.findAll();
        
        return psychologists.stream()
            .map(this::convertPsychologistWithUserAppointment)
            .collect(Collectors.toList());           
    }
    
    // public PsychologistDTO getPsychologistById(String id) {
            
    //     Psychologists psychologist = psychologistRepository.findById(id)
    //     .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id" +id));
         
    //     return convertToDTO(psychologist);
    // }

    public List<AppointmentDTO> appointmentDTO(Psychologists psychologists) {
        AppointmentService appointmentService = new AppointmentService();
        List<Appointments> appointments = appointmentRepository.findByPsychologistID(psychologists.getPsychologistID());
        if(!appointments.isEmpty()) {
            return appointments.stream()
            .map(appointmentService :: covertChildAppointmentDTO)
            .collect(Collectors.toList());
        }
        return Collections.emptyList();

    }
    
    
    public PsychologistDTO convertPsychologistWithUserAppointment(Psychologists psychologist) {
        UserService userService = new UserService();
        Users users = userRepository.findById(psychologist.getUserID())
            .orElseThrow(() -> new ResourceNotFoundException("No user found with psychologistID"));

        UserDTO userDTO = userService.convertToChildDTO(users);
        return 
        PsychologistDTO.builder()
            .psychologistId(psychologist.getPsychologistID())
            .status(psychologist.getStatus().name())
            .specialization(psychologist.getSpecialization())
            .yearsOfExperience(psychologist.getYearsOfExperience())
            .userId(psychologist.getUserID())
            .inforUserofPsychologist(userDTO)
            .appointment(appointmentDTO(psychologist))
            .build();
    }

}
