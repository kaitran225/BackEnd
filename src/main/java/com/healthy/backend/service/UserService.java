package com.healthy.backend.service;

import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.event.EventResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.repository.*;
import com.healthy.backend.enums.Role;
import com.healthy.backend.entity.*;
import com.healthy.backend.mapper.*;

import org.springframework.stereotype.Service;
import lombok.RequiredArgsConstructor;

import java.util.stream.Collectors;
import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.Objects;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final ProgramRepository programRepository;
    private final StudentRepository studentRepository;
    private final AppointmentRepository appointmentRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final PsychologistRepository psychologistRepository;
    private final ProgramParticipationRepository programParticipationRepository;

    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final SurveyMapper surveyMapper;
    private final StudentMapper studentMapper;
    private final AppointmentMapper appointmentMapper;
    private final PsychologistsMapper psychologistsMapper;

    public boolean isEmpty() {
        return userRepository.findAll().isEmpty();
    }

    public List<UsersResponse> getAllUsers() {
        if (isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }
        return userRepository.findAllUsers().stream()
                .map(this::convert)
                .toList();
    }

    public UsersResponse getUserById(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return userMapper.buildBasicUserResponse(user);
    }

    public UsersResponse getUserDetailsById(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return userMapper.buildBasicUserResponse(user);
    }

    public List<SurveyResultsResponse> getUserSurveyResults(String id) {
        List<SurveyResult> surveyResults = surveyResultRepository.findByStudentID(id);
        return surveyMapper.getUserSurveyResults(List.of()); //TEMP
    }

    public UsersResponse updateUser(String userId, Users updatedUser) {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));

        boolean hasChanges = false;

        if (!existingUser.getFullName().equals(updatedUser.getFullName())) {
            existingUser.setFullName(updatedUser.getFullName());
            hasChanges = true;
        }
        if (!existingUser.getEmail().equals(updatedUser.getEmail())) {
            existingUser.setEmail(updatedUser.getEmail());
            hasChanges = true;
        }
        if (!existingUser.getPhoneNumber().equals(updatedUser.getPhoneNumber())) {
            existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
            hasChanges = true;
        }

        if (!hasChanges) {
            return null; // No changes detected
        }

        existingUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(existingUser);

        return convert(existingUser); // Convert entity to response DTO
    }

    public List<AppointmentResponse> getUserAppointments(String id) {
        Users user = userRepository.findById(id).orElseThrow();
        PsychologistResponse psychologistResponse;
        StudentResponse studentResponse;
        List<Appointments> appointmentsList = null;

        if (!user.getRole().equals(Role.STUDENT)
                && !user.getRole().equals(Role.PSYCHOLOGIST)) {
            return null;
        }
        if (user.getRole().equals(Role.STUDENT)) {
            studentResponse = studentMapper.buildStudentResponse(
                    studentRepository.findByUserID(id)
            );
            appointmentsList = appointmentRepository.findByStudentID(
                    studentResponse.getStudentId()
            );
        }
        if (user.getRole().equals(Role.PSYCHOLOGIST)) {
            psychologistResponse = psychologistsMapper.buildPsychologistResponse(
                    psychologistRepository.findByUserID(id)
            );
            appointmentsList = appointmentRepository
                    .findByPsychologistID(psychologistResponse.getPsychologistId());
        }

        return appointmentsList.stream()
                .map(appointment ->
                        appointmentMapper.buildAppointmentResponse(
                                appointment,
                                psychologistsMapper.buildPsychologistResponse(
                                        Objects.requireNonNull(psychologistRepository.findById(
                                                appointment.getPsychologistID()).orElse(null))),
                                studentMapper.buildStudentResponse(
                                        Objects.requireNonNull(studentRepository.findById(
                                                appointment.getStudentID()).orElse(null)))
                        ))
                .collect(Collectors.toList());
    }

    private UsersResponse convert(Users user) {
        List<StudentResponse> childrenList = null;
        List<AppointmentResponse> appointmentsResponseList = null;
        List<SurveyResultsResponse> surveyResultsResponseList;
        PsychologistResponse psychologistResponse = null;
        StudentResponse studentResponse = null;

        switch (user.getRole()) {
            case Role.STUDENT -> {
                Students students = studentRepository.findByUserID(user.getUserId());
                surveyResultsResponseList = getUserSurveyResults(user.getUserId());
                studentResponse = studentMapper.buildStudentResponse(students, surveyResultsResponseList);
                appointmentsResponseList = getUserAppointments(user.getUserId());
            }
            case Role.PARENT -> {
                Parents parent = parentRepository.findByUserIDWithStudents(user.getUserId());
                childrenList = parent.getStudents().stream()
                        .map(studentMapper::buildStudentResponse)
                        .collect(Collectors.toList());
            }
            case Role.PSYCHOLOGIST -> {
                Psychologists psychologists = psychologistRepository.findByUserID(user.getUserId());
                psychologistResponse = psychologistsMapper.buildPsychologistResponse(psychologists);
            }
            case Role.MANAGER -> {
                break;
            }
        }
        return userMapper.buildUserDetailsResponse(
                user,
                psychologistResponse,
                studentResponse,
                appointmentsResponseList,
                childrenList
        );
    }

    public EventResponse getAllEvents(String userId) {

        if (userId == null) {
            throw new ResourceNotFoundException("User not found");
        }

        Users users = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        List<Appointments> appointments = List.of();
        List<Programs> programs = List.of();

        if (users.getRole().equals(Role.STUDENT)) {
            String studentID = studentRepository.findByUserID(userId).getStudentID();
            appointments = appointmentRepository.findByStudentID(studentID);
            programs = programParticipationRepository.findByStudentID(studentID)
                    .stream()
                    .map(participation -> programRepository
                            .findById(participation.getProgram().getProgramID())
                            .orElseThrow(() -> new ResourceNotFoundException("Program not found")))
                    .toList();
        }
        if (users.getRole().equals(Role.PSYCHOLOGIST)) {
            String psychologistID = psychologistRepository.findByUserID(userId).getPsychologistID();
            appointments = appointmentRepository.findByPsychologistID(psychologistID);
            programs = programRepository.findByFacilitatorID(psychologistID);
        }
        if (users.getRole().equals(Role.MANAGER)) {
            appointments = appointmentRepository.findAll();
            programs = programRepository.findAll();
        }
        appointments = appointments.stream()
                .filter(appointment -> appointment.getTimeSlot().getSlotDate().isAfter(LocalDate.now().minusDays(1)))
                .toList();

        programs = programs.stream()
                .filter(program -> program.getStartDate().isAfter(LocalDate.now().minusDays(1))).toList();
        if (appointments.isEmpty() && programs.isEmpty()) {
            return null;
        }
        return switch (users.getRole()) {
            case STUDENT -> eventMapper.buildStudentEventResponse(appointments, programs, userId);
            case PSYCHOLOGIST -> eventMapper.buildPsychologistEventResponse(appointments, programs, userId);
            case MANAGER -> eventMapper.buildManagerEventResponse(appointments, programs, userId);
            default -> throw new ResourceNotFoundException("User not found with id: " + userId);
        };
    }

    public void deactivateUser(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(false);
        userRepository.save(user);
    }

    public void reactivateUser(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(true);
        userRepository.save(user);
    }

    public void updateUserRole(String userId, String role) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(Role.valueOf(role));
        userRepository.save(user);
    }

    public void sendUserNotification(String userId, String message) {
        System.out.println("Notification sent to user " + userId + ": " + message);
    }

    public String getUserDashboard(String userId) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        return "Dashboard data for user: " + user.getUserId();
    }

    public String exportUserData(String userId, String format) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return "Exporting user data for " + user.getUserId() + " in format: " + format;
    }

    public void submitFeedback(String userId, String feedback) {
        System.out.println("Feedback from user " + userId + ": " + feedback);
    }

    public List<UsersResponse> searchUsers(String name) {
        return userRepository.findByFullNameContaining(name).stream()
                .map(userMapper::buildBasicUserResponse)
                .toList();
    }

    public boolean deleteUser(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        if (user.isDeleted()) {
            return false;
        }
        user.setDeleted(false);
        userRepository.save(user);
        return true;
    }
}