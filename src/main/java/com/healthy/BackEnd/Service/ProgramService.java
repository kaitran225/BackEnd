package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.DTO.Programs.ProgramParticipationResponse;
import com.healthy.BackEnd.Entity.ProgramParticipation;
import com.healthy.BackEnd.Entity.Programs;
import com.healthy.BackEnd.Entity.Users;
import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Repository.ProgramParticipationRepository;
import com.healthy.BackEnd.Repository.ProgramRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
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
