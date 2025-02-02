package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.DTO.PsychologistDTO;
import com.healthy.BackEnd.Entity.Psychologists;

public class PsychologistService {

    public PsychologistDTO convertToDTO(Psychologists psychologist) {
        return PsychologistDTO.builder()
                .psychologistId(psychologist.getPsychologistID())
                .status(psychologist.getStatus().name())
                .specialization(psychologist.getSpecialization())
                .yearsOfExperience(psychologist.getYearsOfExperience())
                .userId(psychologist.getUserID())
                .build();
    }
}
