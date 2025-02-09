package com.healthy.backend.service;

import com.healthy.backend.dto.programs.ProgramParticipationResponse;
import com.healthy.backend.entity.ProgramParticipation;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.exception.ResourceNotFoundException;
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

    private final StudentService studentService;

    public List<Programs> getAllPrograms() {
        List<Programs> programs = programRepository.findAll();
        return programs.isEmpty() ? List.of() : programs; // Return an empty list instead of null
    }

    public List<Programs> getProgramsByUserId(String userId) {

        String studentID = studentService.getStudentByUserId(userId).getStudentID();

        List<ProgramParticipation> participations = programParticipationRepository.findByStudentID(studentID);

        if (participations.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No programs found for student ID: " + studentID
            );
        }
        return participations.stream()
                .map(ProgramParticipation::getProgram)
                .toList();
    }

    private ProgramParticipationResponse covert(ProgramParticipation participation) {
        return ProgramParticipationResponse.builder()
        .       programID(participation.getProgram().getProgramID())
        .       programName(participation.getProgram().getProgramName())
        .       category(participation.getProgram().getCategory())
        .       description(participation.getProgram().getDescription())
        .       numberParticipants(participation.getProgram().getNumberParticipants())
        .       duration(participation.getProgram().getDuration())
        .       status(participation.getStatus())
        .createdAt(participation.getProgram().getCreatedAt())
                .build();
    }
}
