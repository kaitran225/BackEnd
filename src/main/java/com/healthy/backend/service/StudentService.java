package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.dto.student.StudentRequest;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.survey.SurveysResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.AppointmentStatus;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.*;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final SurveyRepository surveyRepository;
    private final StudentRepository studentRepository;
    private final ProgramRepository programRepository;
    private final AppointmentRepository appointmentsRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final ProgramParticipationRepository programParticipationRepository;

    private final SurveyMapper surveyMapper;
    private final StudentMapper studentMapper;
    private final ProgramMapper programMapper;
    private final AppointmentMapper appointmentMapper;

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

    @Transactional
    public StudentResponse updateStudent(StudentRequest student) {
        Students existingStudent = studentRepository.findById(student.getStudentID())
                .orElseThrow(() -> new ResourceNotFoundException("No student found with id: " + student.getStudentID()));

        existingStudent.setGrade(student.getGrade());
        existingStudent.setClassName(student.getClassName());
        existingStudent.setSchoolName(student.getSchoolName());

        Users user = existingStudent.getUser();
        user.setFullName(student.getName());
        user.setEmail(student.getEmail());
        user.setPhoneNumber(student.getPhoneNumber());
        user.setAddress(student.getAddress());
        user.setUpdatedAt(LocalDateTime.now());

        studentRepository.save(existingStudent);

        return studentMapper.buildStudentResponse(existingStudent);
    }

//    public List<SurveyResultsResponse> getSurveyResults(String id) {
//        List<SurveyQuestionOptionsChoices> surveyResults = surveyResultRepository.findByStudentID(id);
//        return surveyMapper.getUserSurveyResults(surveyResults);
//    }
//
//    public List<SurveysResponse> getPendingSurveys(String id) {
//        List<Surveys> surveys = surveyRepository.findAll();
//        if (surveys.isEmpty()) {
//            throw new ResourceNotFoundException("No surveys found");
//        }
//        return surveys
//                .stream()
//                .filter(survey -> {
//                    return surveyResultRepository.findByStudentID(id).stream().noneMatch(sr -> sr.getQuestion().getSurveyID().equals(survey.getSurveyID()));
//                })
//                .map(surveyMapper::buildSurveysResponse).toList();
//    }
//
//    public List<SurveysResponse> getCompletedSurveys(String id) {
//        List<Surveys> surveyResults = surveyRepository.findAll();
//        return surveyResults
//                .stream()
//                .filter(survey -> {
//                    return surveyResultRepository.findByStudentID(id).stream().anyMatch(sr -> sr.getQuestion().getSurveyID().equals(survey.getSurveyID()));
//                }).map(surveyMapper::buildSurveysResponse).toList();
//    }

    public List<ProgramsResponse> getEnrolledPrograms(String studentId) {
        return programParticipationRepository.findByStudentID(studentId).stream()
                .map(p -> programMapper.buildProgramResponse(
                        programRepository
                                .findById(p.getProgram().getProgramID())
                                .orElseThrow(() -> new ResourceNotFoundException("Program not found")),
                        getStudentsByProgram(p.getProgram().getProgramID())
                )).toList();
    }

    public List<ProgramsResponse> getCompletedPrograms(String studentId) {
        List<ProgramParticipation> participation = programParticipationRepository.findByStudentID(studentId);
        if (participation.isEmpty()) {
            throw new ResourceNotFoundException("No enrolled programs found");
        }
        return participation.stream()
                .map(p -> programMapper.buildProgramResponse(
                        programRepository
                                .findById(p.getProgram().getProgramID())
                                .orElseThrow(() -> new ResourceNotFoundException("Program not found")),
                        getStudentsByProgram(p.getProgram().getProgramID())
                ))
                .toList();
    }

    public List<AppointmentResponse> getAppointments(String studentId) {
        List<Appointments> appointments = appointmentsRepository.findByStudentID(studentId);
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        return appointments.stream()
                .map(appointmentMapper::buildAppointmentResponse).toList();
    }

    public List<AppointmentResponse> getUpcomingAppointments(String studentId) {
        List<Appointments> appointments = appointmentsRepository.findByStudentID(studentId)
                .stream()
                .filter(appointment -> appointment.getTimeSlot().getSlotDate().isAfter(LocalDate.now()))
                .toList();
        if (appointments.isEmpty()) {
            throw new ResourceNotFoundException("No appointments found");
        }
        return appointments.stream()
                .filter(appointment -> appointment.getStatus() == AppointmentStatus.SCHEDULED)
                .map(appointmentMapper::buildAppointmentResponse).toList();
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
}
