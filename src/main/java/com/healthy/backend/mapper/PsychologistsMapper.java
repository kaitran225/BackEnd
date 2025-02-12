package com.healthy.backend.mapper;

import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.entity.Psychologists;
import org.springframework.stereotype.Component;

@Component
public class PsychologistsMapper {

    public  PsychologistResponse buildPsychologistResponse(Psychologists psychologist) {
        return PsychologistResponse.builder()
                .psychologistId(psychologist.getPsychologistID())
                .status(psychologist.getStatus().name())
                .specialization(psychologist.getSpecialization())
                .yearsOfExperience(psychologist.getYearsOfExperience())
                .build();
    }
}
