package com.healthy.backend.service;

import com.healthy.backend.dto.PsychologistDTO;
import com.healthy.backend.entity.Psychologists;

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
