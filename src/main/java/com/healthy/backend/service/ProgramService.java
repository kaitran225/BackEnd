package com.healthy.backend.service;

import com.healthy.backend.entity.ProgramParticipation;
import com.healthy.backend.entity.Programs;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.repository.ProgramParticipationRepository;
import com.healthy.backend.repository.ProgramRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProgramService {

    @Autowired
    private ProgramRepository programRepository;

    @Autowired
    private ProgramParticipationRepository programParticipationRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private StudentService studentService;

    public List<Programs> getAllPrograms() {
        if (programRepository.findAll().isEmpty()) return null;
        return programRepository.findAll();
    }

    public List<Programs> getProgramsByUserId(String userId) {
        if (!userService.isUserExist(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }

        String studentID = studentService
                .getStudentByUserId(userId)
                .getStudentID()
                .trim();

        List<ProgramParticipation> programParticipationList =
                programParticipationRepository.findByStudentID(studentID);

        if (programParticipationList.isEmpty()) {
            throw new ResourceNotFoundException(
                    "No programs found for student with id: " + studentID + " for user with id: " + userId
            );
        }

        return programParticipationList.stream()
                .map(ProgramParticipation::getProgram)
                .toList();
    }
}
