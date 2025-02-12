package com.healthy.backend.service;

import com.healthy.backend.dto.programs.ProgramParticipationResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.entity.ProgramParticipation;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.ProgramMapper;
import com.healthy.backend.repository.ProgramParticipationRepository;
import com.healthy.backend.repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;

    private final ProgramParticipationRepository programParticipationRepository;

    private final ProgramMapper programMapper;

    public List<ProgramsResponse> getAllPrograms() {
        List<Programs> programs = programRepository.findAll();
        if(programs.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return programs.stream().map(programMapper::buildProgramResponse).toList();
    }
}
