package com.healthy.backend.service;

import com.healthy.backend.dto.programs.*;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.ParticipationStatus;
import com.healthy.backend.enums.ProgramStatus;
import com.healthy.backend.enums.ProgramType;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceAlreadyExistsException;
import com.healthy.backend.exception.ResourceInvalidException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.ProgramMapper;
import com.healthy.backend.mapper.StudentMapper;
import com.healthy.backend.repository.*;
import com.healthy.backend.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ProgramService {

    private final UserRepository userRepository;
    private final TagsRepository tagsRepository;
    private final ProgramRepository programRepository;
    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final PsychologistRepository psychologistRepository;
    private final ProgramScheduleRepository programScheduleRepository;
    private final ProgramParticipationRepository programParticipationRepository;

    private final ProgramMapper programMapper;
    private final StudentMapper studentMapper;

    private final GeneralService __;
    private final NotificationService notificationService;
    private final TokenService tokenService;

    public List<ProgramsResponse> getAllProgramsDetails(HttpServletRequest request) {
        if (!tokenService.validateRole(request, Role.MANAGER)
                && !tokenService.validateRole(request, Role.PSYCHOLOGIST)) {
            throw new OperationFailedException("You don't have permission to view data for all programs");
        }
        List<Programs> programs = programRepository.findAll();
        if (programs.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return programs.stream().map(
                program -> programMapper.buildProgramsDetailsResponse(
                        program,
                        getStudentsByProgram(program.getProgramID())
                )).toList();
    }

    public List<ProgramsResponse> getAllPrograms(HttpServletRequest request) {
        List<Programs> programs = programRepository.findAll();
        if (programs.isEmpty()) throw new ResourceNotFoundException("No programs found");
        return programs.stream().map(program -> {
            List<StudentResponse> enrolled = getStudentsByProgram(program.getProgramID());
            return programMapper.buildProgramResponse(program, enrolled);
        }).toList();
    }

    public ProgramTagResponse createProgramTag(ProgramTagRequest programTagRequest, HttpServletRequest request) {
        if (!tokenService.validateRole(request, Role.MANAGER)
                && !tokenService.validateRole(request, Role.PSYCHOLOGIST)) {
            throw new OperationFailedException("You don't have permission to create this program tag");
        }
        if (tagsRepository.existsByTagName(programTagRequest.getTagName())) {
            throw new ResourceAlreadyExistsException("Tag already exists");
        }
        String tagId = __.generateTagID();
        Tags newTag = new Tags(tagId, programTagRequest.getTagName());
        tagsRepository.save(newTag);
        return programMapper.buildProgramTagResponse(newTag);
    }

    public ProgramsResponse createProgram(ProgramsRequest programsRequest,HttpServletRequest request) {
        if (!tokenService.validateRole(request, Role.MANAGER)
                && !tokenService.validateRole(request, Role.PSYCHOLOGIST)) {
            throw new OperationFailedException("You don't have permission to create this program");
        }
        String programId = __.generateProgramID();
        Optional<Users> staffUser = userRepository.findById(programsRequest.getUserId());
        if (staffUser.isEmpty()) {
            throw new ResourceNotFoundException("User with ID " + programsRequest.getUserId() + " not found.");
        }

        HashSet<Tags> tags = programsRequest.getTags()
                .stream()
                .map(String::toUpperCase)
                .map(tag -> tagsRepository.findById(tag)
                        .orElseThrow(() -> new ResourceNotFoundException("Tag not found with ID: " + tag)))
                .collect(Collectors.toCollection(HashSet::new));

        Department department = departmentRepository.findById(programsRequest.getDepartmentId())
                .orElseThrow(() -> new ResourceNotFoundException("Department not found with ID: " + programsRequest.getDepartmentId()));
        Psychologists facilitator = psychologistRepository.findById(programsRequest.getFacilitatorId())
                .orElseThrow(() -> new ResourceNotFoundException("Psychologist not found with ID: " + programsRequest.getFacilitatorId()));
        programRepository.save(new Programs(
                programId,
                programsRequest.getName(),
                programsRequest.getDescription(),
                programsRequest.getNumberParticipants(),
                programsRequest.getDuration(),
                ProgramStatus.valueOf(programsRequest.getStatus().toUpperCase()),
                department,
                facilitator,
                tags,
                LocalDate.parse(programsRequest.getStartDate()),
                programsRequest.getMeetingLink(),
                ProgramType.valueOf(programsRequest.getType().toUpperCase())
        ));
        return getProgramById(programId, request);
    }

    public ProgramsResponse getProgramById(String programId, HttpServletRequest request) {
        Programs program = programRepository.findById(programId).orElse(null);
        if (program == null) throw new ResourceNotFoundException("Program not found");
        return programMapper.buildProgramResponse(program, getStudentsByProgram(programId));
    }

    public List<ProgramTagResponse> getProgramTags(HttpServletRequest request) {
        List<Tags> tags = tagsRepository.findAll();
        if (tags.isEmpty()) throw new ResourceNotFoundException("No tags found");
        return tags.stream().map(programMapper::buildProgramTagResponse).toList();
    }

    public boolean registerForProgram(ProgramParticipationRequest programParticipationRequest, HttpServletRequest request) {
        if (!tokenService.validateRole(request, Role.STUDENT)) {
            throw new OperationFailedException("You don't have permission to register for a program");
        }
        Programs program = programRepository.findById(programParticipationRequest.getProgramID())
                .orElseThrow(() -> new ResourceNotFoundException("Program not found"));

        if (isJoined(programParticipationRequest)) {
            throw new ResourceAlreadyExistsException("Student is already registered for this program");
        }
        String programParticipationId = __.generateParticipantID();
        programParticipationRepository.save(
                new ProgramParticipation(
                        programParticipationId,
                        programParticipationRequest.getStudentID(),
                        programParticipationRequest.getProgramID(),
                        ParticipationStatus.JOINED,
                        LocalDate.now()
                )
        );

        // Send notification
        notificationService.createProgramNotification(
                studentRepository.findById(programParticipationRequest.getStudentID()).get().getUserID(),
                "New Program Registration",
                "You have a new program registration for " + program.getProgramName(),
                program.getProgramID());

        return programParticipationRepository.findById(programParticipationId).isPresent();
    }

    public String getProgramStatus(String programId, HttpServletRequest request) {
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
        if (participation.getStatus().equals(ParticipationStatus.CANCELLED))
            throw new ResourceAlreadyExistsException("Participation is already cancelled");

        if (participation.getStatus().equals(ParticipationStatus.COMPLETED)) {
            throw new ResourceAlreadyExistsException("Participation is already completed");
        }

        participation.setStatus(ParticipationStatus.CANCELLED);
        ProgramParticipation updatedParticipation = programParticipationRepository.save(participation);

        notificationService.createProgramNotification(
                participation.getStudent().getUser().getUserId(),
                "New Program Registration",
                "You have a new program registration for " + participation.getProgram().getProgramName(),
                participation.getProgram().getProgramID());

        return updatedParticipation.getStatus().equals(ParticipationStatus.CANCELLED);
    }

    public List<ProgramsResponse> getEnrolledPrograms(
            String studentId,
            HttpServletRequest request) {
        String finalStudentId = validateStudentID(request, studentId);
        if (!tokenService.getRoleID(tokenService.retrieveUser(request)).equals(finalStudentId)
                && !tokenService.validateRole(request, Role.MANAGER)) {
            throw new OperationFailedException("You don't have permission to view this student's enrolled programs");
        }
        List<String> programIDList = programParticipationRepository.findProgramIDsByStudentID(finalStudentId);
        if (programIDList.isEmpty()) {
            return new ArrayList<>();
        }
        return programIDList.stream()
                .map(programID -> programMapper.buildProgramResponse(
                        programRepository.findById(programID).orElseThrow(() -> new ResourceNotFoundException("Program not found")),
                        getStudentsByProgram(programID)
                ))
                .toList();
    }

    @Transactional
    public boolean deleteProgram(String programId, HttpServletRequest request) {
        if (programRepository.existsById(programId)) {
            throw new ResourceNotFoundException("Program not found");
        }
        if (!tokenService.validateRole(request, Role.MANAGER)
                && !tokenService.validateRole(request, Role.PSYCHOLOGIST)) {
            throw new OperationFailedException("You don't have permission to delete this program");
        }
        if (!programRepository.existsById(programId)) {
            throw new ResourceNotFoundException("Program not found with ID: " + programId);
        }

        Programs program = programRepository.findById(programId).orElseThrow(() -> new ResourceNotFoundException("Program not found"));
        program.setStatus(ProgramStatus.DELETED);
        programRepository.save(program);

        if (programRepository.findById(programId).isPresent()) return false;
        return programRepository.findById(programId).isEmpty();
    }

    public ProgramsResponse getProgramParticipants(String programId,HttpServletRequest request) {
        if (!tokenService.validateRole(request, Role.MANAGER)
                && !tokenService.validateRole(request, Role.PSYCHOLOGIST)) {
            throw new OperationFailedException("You don't have permission to get participants of this program");
        }
        Programs program = programRepository.findById(programId).orElse(null);
        if (program == null) throw new ResourceNotFoundException("Program not found");
        List<StudentResponse> studentResponses = getStudentsByProgram(programId)
                .stream()
                .filter(studentResponse -> {
                    ProgramParticipation programParticipation = programParticipationRepository
                            .findByProgramIDAndStudentID(programId, studentResponse.getStudentId());
                    return programParticipation != null && programParticipation.getStatus().equals(ParticipationStatus.JOINED);
                })
                .collect(Collectors.toList());
        if (studentResponses.isEmpty()) programMapper.buildProgramsParticipantResponse(program, new ArrayList<>());
        return programMapper.buildProgramsParticipantResponse(program, studentResponses);
    }

    private List<StudentResponse> getStudentsByProgram(String programId) {
        List<String> studentIDs = programParticipationRepository.findStudentIDsByProgramID(programId);
        if (studentIDs.isEmpty()) {
            return new ArrayList<>();
        }
        return studentIDs.stream()
                .map(studentRepository::findByStudentID)
                .map(studentMapper::buildStudentResponse)
                .peek(studentResponse -> {
                    ProgramParticipation programParticipation = programParticipationRepository
                            .findByProgramIDAndStudentID(programId, studentResponse.getStudentId());
                    if (programParticipation != null) {
                        studentResponse.setProgramStatus(programParticipation.getStatus().name());
                    }
                })
                .toList();
    }

    private boolean isJoined(ProgramParticipationRequest programParticipationRequest) {
        return programParticipationRepository.findByProgramIDAndStudentID(
                programParticipationRequest.getProgramID(), programParticipationRequest.getStudentID()
        ) != null;
    }

    private String validateStudentID(HttpServletRequest request, String studentId) {
        if (studentId == null) {
            return tokenService.getRoleID(tokenService.retrieveUser(request));
        }
        if (!studentRepository.existsById(studentId)) {
            return tokenService.getRoleID(tokenService.retrieveUser(request));
        }
        return studentId;
    }
}
