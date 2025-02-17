//package com.healthy.backend.controller;
//
//import com.healthy.backend.dto.student.StudentRequest;
//import com.healthy.backend.dto.student.StudentResponse;
//import com.healthy.backend.dto.survey.SurveyResultsResponse;
//import com.healthy.backend.entity.Students;
//import com.healthy.backend.exception.ResourceNotFoundException;
//import com.healthy.backend.service.StudentService;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//
//@RestController
//@RequestMapping("/api/students")
//public class StudentController {
//
//    private final StudentService studentService;
//
//    public StudentController(StudentService studentService) {
//        this.studentService = studentService;
//    }
//
//    // Get student by ID
//    @GetMapping("/{studentId}")
//    public ResponseEntity<StudentResponse> getStudentById(@PathVariable String studentId) {
//        return ResponseEntity.ok(studentService.getStudentById(studentId));
//    }
//
//    // Update student details
//    @PutMapping("/{studentId}/update")
//    public ResponseEntity<StudentResponse> updateStudent(@PathVariable String studentId,
//                                                         @RequestBody StudentRequest student) {
//        return ResponseEntity.ok(studentService.updateStudent(student));
//    }
//
//    // Delete student account
//    @DeleteMapping("/{studentId}")
//    public ResponseEntity<Void> deleteStudent(@PathVariable String studentId) {
//        studentService.deleteStudent(studentId);
//        return ResponseEntity.noContent().build();
//    }
//
//    // Get student survey results
//    @GetMapping("/{studentId}/surveys")
//    public ResponseEntity<List<SurveyResultsResponse>> getStudentSurveys(@PathVariable String studentId) {
//        if(!studentService.isStudentExist(studentId))
//            throw new ResourceNotFoundException("No student found with id: " + studentId);
//        return ResponseEntity.ok(studentService.getSurveyResults(studentId));
//    }
//
//    // Get pending surveys for student
//    @GetMapping("/{studentId}/surveys/pending")
//    public ResponseEntity<List<String>> getPendingSurveys(@PathVariable String studentId) {
//        return ResponseEntity.ok(studentService.getPendingSurveys(studentId));
//    }
//
//    // Get programs assigned to student
//    @GetMapping("/{studentId}/programs")
//    public ResponseEntity<List<String>> getStudentPrograms(@PathVariable String studentId) {
//        return ResponseEntity.ok(studentService.getStudentPrograms(studentId));
//    }
//
//    // Get completed programs
//    @GetMapping("/{studentId}/programs/completed")
//    public ResponseEntity<List<String>> getCompletedPrograms(@PathVariable String studentId) {
//        return ResponseEntity.ok(studentService.getCompletedPrograms(studentId));
//    }
//
//    // Get student appointments
//    @GetMapping("/{studentId}/appointments")
//    public ResponseEntity<List<String>> getStudentAppointments(@PathVariable String studentId) {
//        return ResponseEntity.ok(studentService.getStudentAppointments(studentId));
//    }
//
//    // Get upcoming student appointments
//    @GetMapping("/{studentId}/appointments/upcoming")
//    public ResponseEntity<List<String>> getUpcomingAppointments(@PathVariable String studentId) {
//        return ResponseEntity.ok(studentService.getUpcomingAppointments(studentId));
//    }
//}
