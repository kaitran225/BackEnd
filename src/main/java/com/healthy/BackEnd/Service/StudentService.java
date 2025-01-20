package com.healthy.BackEnd.Service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.healthy.BackEnd.entity.Students;
import com.healthy.BackEnd.exception.ResourceNotFoundException;
import com.healthy.BackEnd.repository.StudentRepository;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    public boolean isEmpty() {
        return studentRepository.findAll().isEmpty();
    }

    public boolean isStudentExist(String id) {
        if(isEmpty()) throw new ResourceNotFoundException("No students found");
        return studentRepository.existsById(id);
    }

    public List<Students> getAllStudents() {
        if(studentRepository.findAll().isEmpty()) throw new ResourceNotFoundException("No students found");
        return studentRepository.findAll();
    }

    public Students getStudentById(String id) {
        if(!isStudentExist(id)) throw new ResourceNotFoundException("Student not found with id: " + id);
        return studentRepository.findById(id).orElse(null);
    }

    public Students getStudentByUserId(String userId) {
        if(!isStudentExist(userId)) throw new ResourceNotFoundException("Student not found with userId: " + userId);
        return studentRepository.findByUserID(userId);
    }
}

