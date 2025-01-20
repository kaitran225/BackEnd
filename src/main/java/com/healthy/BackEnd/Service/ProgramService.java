package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.entity.ProgramParticipation;
import com.healthy.BackEnd.entity.Programs;
import com.healthy.BackEnd.exception.ResourceNotFoundException;
import com.healthy.BackEnd.repository.ProgramParticipationRepository;
import com.healthy.BackEnd.repository.ProgramRepository;

import jakarta.persistence.AttributeOverride;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
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
        if(programRepository.findAll().isEmpty()) return null;
        return programRepository.findAll();
    }

    public List<Programs> getProgramsByUserId(String userId) {
        if(!userService.isUserExist(userId)) throw new ResourceNotFoundException("User not found with id: " + userId);
        String studentId = studentService.getStudentByUserId(userId).getStudentID();
        if(!studentService.isStudentExist(studentId)) throw new ResourceNotFoundException("Student not found with id: " + studentId  + " for user with id: " + userId);
        List<ProgramParticipation> programParticipations = programParticipationRepository.findByStudentID(studentId);
        if(programParticipations.isEmpty()) throw new ResourceNotFoundException("No programs found for student with id: " + studentId + " for user with id: " + userId);
        List<Programs> programs = new ArrayList<>();
        for(ProgramParticipation programParticipation : programParticipations) {
            programs.add(programParticipation.getProgram());
        }
        return programs;
    }
}
