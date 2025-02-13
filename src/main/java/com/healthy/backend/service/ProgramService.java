package com.healthy.backend.service;

import com.healthy.backend.dto.programs.ProgramParticipationRequest;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.ProgramParticipation;
import com.healthy.backend.exception.ResourceAlreadyExistsException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.mapper.ProgramMapper;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.mapper.StudentMapper;
import com.healthy.backend.repository.ProgramParticipationRepository;
import com.healthy.backend.repository.ProgramRepository;
import com.healthy.backend.repository.StudentRepository;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final ProgramRepository programRepository;

    private final ProgramParticipationRepository programParticipationRepository;

    private final StudentRepository studentRepository;

    private final ProgramMapper programMapper;

    private final StudentMapper studentMapper;

    public List<ProgramsResponse> getAllProgramsDetails() {
        List<Programs> programs = programRepository.findAll();
        if (programs.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return programs.stream().map(
                program -> programMapper.buildProgramsDetailsResponse(
                        program,
                        getStudentsByProgram(program.getProgramID())
        )).toList();
    }

    public List<ProgramsResponse> getAllPrograms() {
        List<Programs> programs = programRepository.findAll();
        if (programs.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return programs.stream().map(programMapper::buildProgramResponse).toList();
    }

    public ProgramsResponse getProgramById(String programId) {
        Programs program = programRepository.findById(programId).orElse(null);
        if (program == null) throw new ResourceNotFoundException("Program not found");
        return programMapper.buildProgramResponse(program);
    }

    public boolean registerForProgram(ProgramParticipationRequest programParticipationRequest) {
        Programs program = programRepository.findById(programParticipationRequest.getProgramID())
                .orElseThrow(() -> new ResourceNotFoundException("Program not found"));
        if (isJoined(programParticipationRequest)) {
            throw new ResourceAlreadyExistsException("Student is already registered for this program");
        }
        String programParticipationId = generateNextProgramParticipationId(programParticipationRepository.findLastParticipationCode());
        programParticipationRepository.save(
                new ProgramParticipation(
                        programParticipationId,
                        programParticipationRequest.getStudentID(),
                        programParticipationRequest.getProgramID(),
                        ProgramParticipation.Status.Joined,
                        LocalDate.now()
                )
        );
        return programParticipationRepository.findById(programParticipationId).isPresent();
    }

    public String getProgramStatus(String programId) {
        Programs program = programRepository.findById(programId)
                .orElseThrow(() -> new ResourceNotFoundException("Program not found"));
        return program.getStatus().name();
    }

    public boolean cancelParticipation(ProgramParticipationRequest programParticipationRequest) {
        if (!isJoined(programParticipationRequest)) {
            throw new ResourceNotFoundException("Participation not found");
        }
        ProgramParticipation participation = programParticipationRepository.findByProgramIDAndStudentID(
                programParticipationRequest.getProgramID(), programParticipationRequest.getStudentID());
        if (participation.getStatus().equals(ProgramParticipation.Status.Cancelled))
            throw new ResourceAlreadyExistsException("Participation is already cancelled");
        if (participation.getStatus().equals(ProgramParticipation.Status.Completed)) {
            throw new ResourceAlreadyExistsException("Participation is already completed");
        }
        participation.setStatus(ProgramParticipation.Status.Cancelled);
        ProgramParticipation updatedParticipation = programParticipationRepository.save(participation);
        return updatedParticipation.getStatus().equals(ProgramParticipation.Status.Cancelled);
    }

    public List<ProgramsResponse> getEnrolledPrograms(String studentId) {
        List<ProgramParticipation> participation = programParticipationRepository.findByStudentID(studentId);
        if (participation.isEmpty()) {
            throw new ResourceNotFoundException("No enrolled programs found");
        }
        return participation.stream()
                .map(p -> programMapper.buildProgramResponse(
                                programRepository
                                        .findById(p.getProgram().getProgramID())
                                        .orElseThrow(() -> new ResourceNotFoundException("Program not found"))
                        ))
                .toList();
    }

    private boolean isJoined(ProgramParticipationRequest programParticipationRequest) {
        return programParticipationRepository.findByProgramIDAndStudentID(
                programParticipationRequest.getProgramID(), programParticipationRequest.getStudentID()
        ) != null;
    }

    private List<StudentResponse> getStudentsByProgram(String programId) {
        List<String> studentIDs = programParticipationRepository.findStudentIDsByProgramID(programId);

        if (studentIDs.isEmpty()) {
            return new ArrayList<>();
        }

        return studentIDs.stream()
                .map(studentRepository::findByStudentID)
                .map(studentMapper::buildStudentResponse)
                .toList();
    }


    private String generateNextProgramParticipationId(String lastCode) {
        if (lastCode == null || lastCode.length() < 3) {
            throw new IllegalArgumentException("Invalid last participation code");
        }
        String prefix = lastCode.substring(0, 2);
        int number = Integer.parseInt(lastCode.substring(2));
        return prefix + String.format("%03d", number + 1);
    }
}
