package com.healthy.backend.service;

import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.entity.Psychologists;

public class PsychologistService {

    public PsychologistResponse convertToDTO(Psychologists psychologist) {
        return PsychologistResponse.builder()
                .psychologistId(psychologist.getPsychologistID())
                .status(psychologist.getStatus().name())
                .specialization(psychologist.getSpecialization())
                .yearsOfExperience(psychologist.getYearsOfExperience())
                .build();
    }
}
