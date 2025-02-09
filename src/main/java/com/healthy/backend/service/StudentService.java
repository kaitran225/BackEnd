package com.healthy.backend.service;

import com.healthy.backend.dto.StudentDTO;
import com.healthy.backend.entity.Students;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public boolean isEmpty() {
        return studentRepository.findAll().isEmpty();
    }

    public boolean isStudentExist(String id) {
        if (isEmpty()) throw new ResourceNotFoundException("No students found");
        return studentRepository.existsById(id);
    }

    public List<Students> getAllStudents() {
        if (studentRepository.findAll().isEmpty()) throw new ResourceNotFoundException("No students found");
        return studentRepository.findAll();
    }

    public Students getStudentById(String id) {
        if (isStudentExist(id)) throw new ResourceNotFoundException("Student not found with id: " + id);
        return studentRepository.findById(id).orElse(null);
    }

    public Students getStudentByUserId(String userId) {
        if (isStudentExist(userId)) throw new ResourceNotFoundException("Student not found with userId: " + userId);
        return studentRepository.findByUserID(userId);
    }

    public StudentDTO convertToDTO(Students student) {
        return StudentDTO.builder()
                .studentId(student.getStudentID())
                .userId(student.getUserID())
                .grade(student.getGrade())
                .className(student.getClassName())
                .schoolName(student.getSchoolName())
                .fullName(student.getUser().getFullName())
                .depressionScore(student.getDepressionScore())
                .anxietyScore(student.getAnxietyScore())
                .stressScore(student.getStressScore())
                .build();
    }

    public StudentDTO convertToChildDTO(Students student) {
        return StudentDTO.builder()
                .userId(student.getUserID())
                .studentId(student.getStudentID())
                .grade(student.getGrade())
                .className(student.getClassName())
                .fullName(student.getUser().getFullName())
                .build();
    }
}

