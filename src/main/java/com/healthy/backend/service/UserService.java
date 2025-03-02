package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.event.EventResponse;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.*;
import com.healthy.backend.repository.*;
import com.healthy.backend.security.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

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

    private final TokenService tokenService;

    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final SurveyMapper surveyMapper;
    private final StudentMapper studentMapper;
    private final ProgramMapper programMapper;
    private final AppointmentMapper appointmentMapper;
    private final PsychologistsMapper psychologistsMapper;

    public boolean isEmpty() {
        return userRepository.findAll().isEmpty();
    }

    public List<UsersResponse> getAllUsers(HttpServletRequest request) {
        if (!tokenService.validateRole(request, Role.MANAGER)) {
            return List.of(userMapper.buildBasicUserResponse(tokenService.retrieveUser(request)));
        }
        if (isEmpty()) {
            throw new ResourceNotFoundException("No users found");
        }
        return userRepository.findAllUsers().stream()
                .map(userMapper::buildBasicUserResponse)
                .toList();
    }

    public UsersResponse getUserById(String userId, HttpServletRequest request) {
        String finalUserId = validateUserID(request, userId);
        if (tokenService.validateUID(request, finalUserId) && !tokenService.validateRole(request, Role.MANAGER)) {
            throw new OperationFailedException("You can not get other users details");
        }
        return userMapper.buildBasicUserResponse(userRepository.findById(finalUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + finalUserId)));
    }

    public UsersResponse getUserDetailsById(String userId, HttpServletRequest request) {
        String finalUserId = validateUserID(request, userId);
        if (tokenService.validateUID(request, finalUserId)
                && !tokenService.validateRole(request, Role.MANAGER)) {
            throw new OperationFailedException("You can not get other users details");
        }
        return convert(userRepository.findById(finalUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + finalUserId)));
    }

    public UsersResponse updateUser(String userId, Users updatedUser, HttpServletRequest request) {
        String finalUserId = validateUserID(request, userId);
        if (tokenService.validateUID(request, finalUserId) && !tokenService.validateRole(request, Role.MANAGER)) {
            throw new OperationFailedException("You can not update other user details");
        }
        Users existingUser = userRepository.findById(finalUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + finalUserId));

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
            return null;
        }

        existingUser.setUpdatedAt(LocalDateTime.now());
        userRepository.save(existingUser);

        return convert(existingUser);
    }

    private UsersResponse convert(Users user) {
        List<StudentResponse> childrenList = null;
        List<AppointmentResponse> appointmentsResponseList = null;
        List<ProgramsResponse> programsResponses = null;
        List<SurveyResultsResponse> surveyResultsResponseList = null;
        PsychologistResponse psychologistResponse = null;
        StudentResponse studentResponse = null;

        switch (user.getRole()) {
            case Role.STUDENT -> {
                Students students = studentRepository.findByUserID(user.getUserId());
                surveyResultsResponseList = getUserSurveyResults(user.getUserId());
                studentResponse = studentMapper.buildStudentResponse(students, surveyResultsResponseList);
                programsResponses = getUserProgramParticipation(user.getUserId());
                appointmentsResponseList = getStudentAppointments(user.getUserId());
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
                appointmentsResponseList = getPsychologistAppointments(user.getUserId());
                programsResponses = getUserProgramFacility(user.getUserId());
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
                programsResponses,
                surveyResultsResponseList,
                childrenList
        );
    }

    public EventResponse getAllEvents(String userId, HttpServletRequest request) {
        String finalUserID = validateUserID(request, userId);
        if (tokenService.validateUID(request, finalUserID) && !tokenService.validateRole(request, Role.MANAGER)) {
            throw new OperationFailedException("You can not get other users events");
        }
        Users users = userRepository.findById(finalUserID)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + finalUserID));
        List<Appointments> appointments = List.of();
        List<Programs> programs = List.of();

        if (users.getRole().equals(Role.STUDENT)) {
            String studentID = studentRepository.findByUserID(finalUserID).getStudentID();
            appointments = appointmentRepository.findByStudentID(studentID);
            programs = programParticipationRepository.findByStudentID(studentID)
                    .stream()
                    .map(participation -> programRepository
                            .findById(participation.getProgram().getProgramID())
                            .orElseThrow(() -> new ResourceNotFoundException("Program not found")))
                    .toList();
        }
        if (users.getRole().equals(Role.PSYCHOLOGIST)) {
            String psychologistID = psychologistRepository.findByUserID(finalUserID).getPsychologistID();
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
            return eventMapper.buildEmptyEventResponse(appointments, programs, finalUserID);
        }

        return switch (users.getRole()) {
            case STUDENT -> eventMapper.buildStudentEventResponse(appointments, programs, finalUserID);
            case PSYCHOLOGIST -> eventMapper.buildPsychologistEventResponse(appointments, programs, finalUserID);
            case MANAGER -> eventMapper.buildManagerEventResponse(appointments, programs, finalUserID);
            default -> throw new ResourceNotFoundException("User not found with id: " + finalUserID);
        };
    }

    public UsersResponse deactivateUser(String userId, HttpServletRequest request) {
        String finalUserId = validateUserID(request, userId);
        if (!tokenService.validateRole(request, Role.MANAGER)) {
            throw new OperationFailedException("You can not deactivate other user account");
        }
        Users user = userRepository.findById(finalUserId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(false);
        userRepository.save(user);
        return userMapper.buildBasicUserResponse(user);
    }

    public UsersResponse reactivateUser(String userId, HttpServletRequest request) {
        String finalUserId = validateUserID(request, userId);
        if (!tokenService.validateRole(request, Role.MANAGER) && tokenService.validateUID(request, finalUserId)) {
            throw new OperationFailedException("You can not reactivate other user account");
        }
        Users user = userRepository.findById(finalUserId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setActive(true);
        userRepository.save(user);
        return userMapper.buildBasicUserResponse(user);
    }

    public UsersResponse updateUserRole(String userId, String role, HttpServletRequest request) {
        if (!tokenService.validateRole(request, Role.MANAGER)) {
            throw new OperationFailedException("You can not update other user role");
        }
        Users user = userRepository.findById(userId).orElseThrow(() -> new ResourceNotFoundException("User not found"));
        user.setRole(Role.valueOf(role));
        userRepository.save(user);
        return userMapper.buildBasicUserResponse(user);
    }

    public String exportUserData(String userId, String format, HttpServletRequest request) {
        Users user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        return "Exporting user data for " + user.getUserId() + " in format: " + format;
    }

    public void submitFeedback(String userId, String feedback, HttpServletRequest request) {
        System.out.println("Feedback from user " + userId + ": " + feedback);
    }

    public List<UsersResponse> searchUsers(String name, HttpServletRequest request) {
        if (!tokenService.validateRole(request, Role.MANAGER)) {
            throw new OperationFailedException("You can not search for users");
        }
        return userRepository.findByFullNameContaining(name).stream()
                .map(userMapper::buildBasicUserResponse)
                .toList();
    }

    public boolean deleteUser(String userId, HttpServletRequest request) {
        if (!tokenService.validateRole(request, Role.MANAGER)) {
           throw new OperationFailedException("You can not delete users");
        }
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        if (user.isDeleted()) {
            return false;
        }
        user.setDeleted(false);
        userRepository.save(user);
        return true;
    }

    public List<AppointmentResponse> getUserAppointment(String userId, HttpServletRequest request) {
        String finalUserId = validateUserID(request, userId);
        if (tokenService.validateUID(request, finalUserId) && !tokenService.validateRole(request, Role.MANAGER)) {
            throw new OperationFailedException("You can not get other users details");
        }
        Users users = userRepository.findById(finalUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + finalUserId));

        if (users.getRole().equals(Role.STUDENT)) {
            return this.getStudentAppointments(finalUserId);
        }
        if (users.getRole().equals(Role.PSYCHOLOGIST)) {
            return this.getPsychologistAppointments(finalUserId);
        }
        if (users.getRole().equals(Role.MANAGER)) {
            return appointmentRepository.findAll().stream()
                    .map(appointmentMapper::buildAppointmentResponse)
                    .toList();
        }
        return null;
    }

    private String validateUserID(HttpServletRequest request, String userId) {
        if(userId == null) {
            return tokenService.retrieveUser(request).getUserId();
        }
        if(!userRepository.existsById(userId)) {
            return tokenService.retrieveUser(request).getUserId();
        }
        return userId;
    }

    private List<AppointmentResponse> getPsychologistAppointments(String userId) {
        Users user = userRepository.findById(userId).orElseThrow();
        List<Appointments> appointmentsList = null;

        if (!user.getRole().equals(Role.PSYCHOLOGIST)) {
            return null;
        }

        appointmentsList = appointmentRepository.findByPsychologistID(
                psychologistRepository.findByUserID(userId).getPsychologistID()
        );

        return appointmentsList != null ? appointmentsList.stream()
                .map(appointment ->
                        appointmentMapper.buildAppointmentResponse(
                                appointment,
                                null,
                                studentMapper.buildStudentResponse(
                                        Objects.requireNonNull(studentRepository.findById(
                                                appointment.getStudentID()).orElse(null)))
                        ))
                .collect(Collectors.toList()) : null;
    }

    private List<AppointmentResponse> getStudentAppointments(String userId) {
        Users user = userRepository.findById(userId).orElseThrow();
        PsychologistResponse psychologistResponse;
        List<Appointments> appointmentsList = null;

        if (!user.getRole().equals(Role.STUDENT)) {
            return null;
        }

        appointmentsList = appointmentRepository
                .findByStudentID(studentRepository.findByUserID(userId).getStudentID());


        return appointmentsList.stream()
                .map(appointment ->
                        appointmentMapper.buildAppointmentResponse(
                                appointment,
                                psychologistsMapper.buildPsychologistResponse(
                                        Objects.requireNonNull(psychologistRepository.findById(
                                                appointment.getPsychologistID()).orElse(null))),
                                null
                        ))
                .collect(Collectors.toList());
    }

    private List<ProgramsResponse> getUserProgramFacility(String id) {
        return programRepository.findByFacilitatorID(id)
                .stream()
                .map(programs -> programMapper.buildProgramsDetailsResponse(programs,
                        programParticipationRepository.findStudentIDsByProgramID(
                                        programs.getProgramID()).stream()
                                .map(studentRepository::findByStudentID)
                                .map(studentMapper::buildStudentResponse)
                                .peek(sr -> {
                                    ProgramParticipation programParticipation = programParticipationRepository
                                            .findByProgramIDAndStudentID(programs.getProgramID(), sr.getStudentId());
                                    if (programParticipation != null) {
                                        sr.setProgramStatus(programParticipation.getStatus().name());
                                    }
                                })
                                .toList()
                )).toList();
    }

    private List<ProgramsResponse> getUserProgramParticipation(String id) {

        StudentResponse studentResponse = studentMapper.buildStudentResponse(
                studentRepository.findByUserID(id)
        );
        return programParticipationRepository.findByStudentID(studentResponse.getStudentId())
                .stream()
                .map(programParticipationResponse -> programMapper.buildProgramResponse(
                        programRepository.findById(programParticipationResponse.getProgramID()).orElseThrow(),
                        programParticipationRepository.findStudentIDsByProgramID(
                                        programParticipationResponse.getProgramID()).stream()
                                .map(studentRepository::findByStudentID)
                                .map(studentMapper::buildStudentResponse)
                                .peek(sr -> {
                                    ProgramParticipation programParticipation = programParticipationRepository
                                            .findByProgramIDAndStudentID(programParticipationResponse.getProgramID(), sr.getStudentId());
                                    if (programParticipation != null) {
                                        sr.setProgramStatus(programParticipation.getStatus().name());
                                    }
                                })
                                .toList()
                ))
                .toList();
    }

    private List<SurveyResultsResponse> getUserSurveyResults(String id) {
        List<SurveyResult> surveyResults = surveyResultRepository.findByStudentID(id);
        return surveyMapper.getUserSurveyResults(List.of()); //TEMP
    }
}