package com.healthy.backend.controller;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.dto.student.StudentRequest;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.survey.SurveysResponse;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.service.StudentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Student Controller", description = "Student related management APIs")
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    // Get student by ID
    @GetMapping("")
    public ResponseEntity<StudentResponse> getStudentById(
            @RequestParam(required = false) String studentId,
            HttpServletRequest request) {
        return ResponseEntity.ok(studentService.getStudentById(studentId, request));
    }

    // Update student details
    @PutMapping("/update")
    public ResponseEntity<StudentResponse> updateStudent(
            @RequestParam(required = false) String studentId,
            @RequestBody StudentRequest student,
            HttpServletRequest request) {
        return ResponseEntity.ok(studentService.updateStudent(studentId, student, request));
    }

    // Get student survey
    @Operation(summary = "Get student survey", description = "Returns a list of student survey.")
    @GetMapping("/surveys")
    public ResponseEntity<List<SurveysResponse>> getStudentSurveys(
            @RequestParam(required = false) String studentId,
            HttpServletRequest request) {
        if (!studentService.isStudentExist(studentId))
            throw new ResourceNotFoundException("No student found with id: " + studentId);
        return ResponseEntity.ok(studentService.getSurveyResults(studentId, request));
    }

    // Get pending surveys for student
    @GetMapping("/surveys/pending")
    public ResponseEntity<List<SurveysResponse>> getPendingSurveys(
            @RequestParam(required = false) String studentId,
            HttpServletRequest request) {
        return ResponseEntity.ok(studentService.getPendingSurveys(studentId, request));
    }

    // Get programs assigned to student
    @GetMapping("/programs")
    public ResponseEntity<List<ProgramsResponse>> getStudentPrograms(
            @RequestParam(required = false) String studentId,
            HttpServletRequest request) {
        return ResponseEntity.ok(studentService.getEnrolledPrograms(studentId, request));
    }

    // Get completed programs
    @GetMapping("/programs/completed")
    public ResponseEntity<List<ProgramsResponse>> getCompletedPrograms(
            @RequestParam(required = false) String studentId,
            HttpServletRequest request) {
        return ResponseEntity.ok(studentService.getCompletedPrograms(studentId, request));
    }

    // Get student appointments
    @GetMapping("/appointments")
    public ResponseEntity<List<AppointmentResponse>> getStudentAppointments(
            @RequestParam(required = false) String studentId,
            HttpServletRequest request) {
        return ResponseEntity.ok(studentService.getAppointments(studentId, request));
    }

    // Get upcoming student appointments
    @GetMapping("/appointments/upcoming")
    public ResponseEntity<List<AppointmentResponse>> getUpcomingAppointments(
            @RequestParam(required = false) String studentId,
            HttpServletRequest request) {
        return ResponseEntity.ok(studentService.getUpcomingAppointments(studentId, request));
    }
}
