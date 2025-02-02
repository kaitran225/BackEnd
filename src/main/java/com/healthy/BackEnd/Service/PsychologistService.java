package com.healthy.BackEnd.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;

import com.atomikos.datasource.ResourceException;
import com.healthy.BackEnd.DTO.PsychologistDTO;
import com.healthy.BackEnd.Entity.Psychologists;
import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Repository.PsychologistRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PsychologistService {

    @Autowired
    PsychologistRepository psychologistRepository;

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
            .map(this::convertToDTO)
            .collect(Collectors.toList());           
    }
    
    


    public PsychologistDTO getPsychogistById(String id) {
            
        Psychologists psychologists = psychologistRepository.findById(id)
        .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id" +id));
        
        
        return convertToDTO(psychologists);

    }      

}
