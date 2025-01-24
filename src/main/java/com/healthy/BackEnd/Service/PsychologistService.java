package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.dto.PsychologistDTO;
import com.healthy.BackEnd.entity.Psychologists;

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
