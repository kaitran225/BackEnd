package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.DTO.Student.StudentResponse;
import com.healthy.BackEnd.Entity.Students;
import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Repository.StudentRepository;
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

    public StudentResponse convertToDTO(Students student) {
        return StudentResponse.builder()
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

    public StudentResponse convertToChildDTO(Students student) {
        return StudentResponse.builder()
                .userId(student.getUserID())
                .studentId(student.getStudentID())
                .grade(student.getGrade())
                .className(student.getClassName())
                .fullName(student.getUser().getFullName())
                .build();
    }
}

