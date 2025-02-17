package com.healthy.backend.service;

import com.healthy.backend.dto.student.StudentRequest;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.survey.SurveysResponse;
import com.healthy.backend.entity.Students;
import com.healthy.backend.entity.SurveyResults;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.StudentMapper;
import com.healthy.backend.mapper.SurveyMapper;
import com.healthy.backend.repository.StudentRepository;
import com.healthy.backend.repository.SurveyRepository;
import com.healthy.backend.repository.SurveyResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final SurveyResultRepository surveyResultRepository;

    private final SurveyRepository surveyRepository;

    private final StudentRepository studentRepository;

    private final StudentMapper studentMapper;

    private final SurveyMapper surveyMapper;

    public boolean isStudentExist(String id) {
        if (!studentRepository.existsById(id))
            throw new ResourceNotFoundException("No students found with id: " + id);
        return true;
    }

    // Get all students
    public List<StudentResponse> getAllStudents() {
        List<Students> students = studentRepository.findAll();
        if (students.isEmpty())
            throw new ResourceNotFoundException("The system have no students yet");
        return students
                .stream()
                .map(studentMapper::buildStudentResponse)
                .toList();
    }

    // Get student by ID
    public StudentResponse getStudentById(String id) {
        Students student = studentRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("No student found with id: " + id));
        return studentMapper.buildStudentResponse(student);
    }

    public StudentResponse updateStudent(StudentRequest student) {
        Students existingStudent = studentRepository.findById(student.getStudentID())
                .orElseThrow(() -> new ResourceNotFoundException("No student found with id: " + student.getStudentID()));
        existingStudent.setFullName(student.getName());
        existingStudent.setEmail(student.getEmail());
        studentRepository.save(existingStudent);
        return studentMapper.buildStudentResponse(existingStudent);
    }

//    public List<SurveysResponse> getSurveyResults(String id) {
//        List<Surveys> surveys = surveyRepository.findAllByStudentID(id);
//        return surveyMapper.getUserSurveyResults(surveys);
//    }

    public List<SurveyResultsResponse> getPendingSurveys(String id) {
        List<SurveyResults> surveyResults = surveyResultRepository.findByStudentID(id);
        return surveyMapper.getUserSurveyResults(surveyResults);
    }

    public void createStudent(StudentRequest student) {
        studentRepository.save(studentMapper.buildStudentEntity(student));
    }

    public void deleteStudent(String studentId) {
        studentRepository.deleteById(studentId);
    }
}
