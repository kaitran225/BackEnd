package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.event.EventResponse;
import com.healthy.backend.dto.event.ProgramWithStudents;
import com.healthy.backend.dto.programs.ProgramsResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.survey.response.SurveyResultsResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.ParticipationStatus;
import com.healthy.backend.enums.Role;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.*;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class ParentService {

    private final UserRepository userRepository;
    private final ParentRepository parentRepository;
    private final StudentRepository studentRepository;
    private final ProgramRepository programRepository;
    private final AppointmentRepository appointmentRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final PsychologistRepository psychologistRepository;
    private final ProgramParticipationRepository programParticipationRepository;
    private final ProgramScheduleRepository programScheduleRepository;

    private final UserMapper userMapper;
    private final EventMapper eventMapper;
    private final StudentMapper studentMapper;
    private final ProgramMapper programMapper;
    private final AppointmentMapper appointmentMapper;
    private final PsychologistsMapper psychologistsMapper;

    public EventResponse getAllChildrenEvents(String userId) {
        if (!userRepository.existsById(userId)) {
            throw new ResourceNotFoundException("User not found with id: " + userId);
        }
        return getChildrenEvent(userId);
    }

    public UsersResponse getParentDetails(String userId) {
        Users user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return getParentDetails(user);
    }

    private UsersResponse getParentDetails(Users user) {
        Parents parent = parentRepository.findByUserID(user.getUserId());
        return userMapper.buildParentUserResponse(
                user,
                parent.getStudents().stream()
                        .map(students -> {
                            return getStudentDetails(students.getUser());
                        })
                        .toList());
    }

    private UsersResponse getStudentDetails(Users user) {
        List<SurveyResultsResponse> surveyResultsResponseList = getUserSurveyResults(user.getUserId());
        return userMapper.buildUserDetailsResponse(
                user,
                null,
                studentMapper.buildStudentResponse(studentRepository
                        .findByUserID(user.getUserId()), surveyResultsResponseList),
                getStudentAppointments(user.getUserId()),
                getUserProgramParticipation(user.getUserId()),
                surveyResultsResponseList,
                null);
    }

    private List<SurveyResultsResponse> getUserSurveyResults(String id) {
        List<SurveyResult> surveyResults = surveyResultRepository.findByStudentID(id);
        if (surveyResults.isEmpty()) {
            return List.of();
        }
        return List.of();
    }

    private List<ProgramsResponse> getUserProgramParticipation(String userId) {
        String student = studentRepository.findByUserID(userId).getStudentID();
        List<ProgramParticipation> participation = programParticipationRepository
                .findActiveByStudentID(student, ParticipationStatus.CANCELLED);
        return participation.stream()
                .map(programParticipation -> {
                    ProgramSchedule programSchedule = programScheduleRepository
                            .findByProgramID(programParticipation.getProgramID()).getLast();
                    Integer activeStudentsCount = getActiveStudentsByProgram(programParticipation.getProgramID()).size();
                    return programMapper.buildProgramResponse(
                            programParticipation.getProgram(),
                            activeStudentsCount,
                            programSchedule,
                            programParticipation.getStatus().name()
                    );
                })
                .toList();
    }

    private List<AppointmentResponse> getStudentAppointments(String userId) {
        Users user = userRepository.findById(userId).orElseThrow();
        if (!user.getRole().equals(Role.STUDENT)) {
            return null;
        }
        return appointmentRepository
                .findByStudentID(studentRepository.findByUserID(userId).getStudentID())
                .stream()
                .map(appointment ->
                        appointmentMapper.buildAppointmentResponse(
                                appointment,
                                psychologistsMapper.buildPsychologistResponse(
                                        psychologistRepository.findByPsychologistID(
                                                appointment.getPsychologistID())),
                                null
                        ))
                .toList();
    }

    private List<StudentResponse> getActiveStudentsByProgram(String programId) {
        List<String> studentIDs = programParticipationRepository.findActiveStudentIDsByProgramID(programId, ParticipationStatus.CANCELLED);

        return studentIDs.stream()
                .map(studentRepository::findByStudentID)
                .map(studentMapper::buildStudentResponse)
                .peek(studentResponse -> {
                    ProgramParticipation programParticipation = programParticipationRepository
                            .findByProgramIDAndStudentID(programId, studentResponse.getStudentId()).getLast();
                    if (programParticipation != null) {
                        studentResponse.setProgramStatus(programParticipation.getStatus().name());
                    }
                })
                .toList();
    }

    private EventResponse getChildrenEvent(String userId) {

        Parents parent = parentRepository.findByUserID(userId);

        Set<Students> studentsList = parent.getStudents();

        List<Appointments> appointments = new ArrayList<>();
        List<ProgramWithStudents> programs = new ArrayList<>();
        for (Students student : studentsList) {
            appointments.addAll(appointmentRepository.findByStudentID(student.getStudentID()).stream()
                    .filter(appointment -> appointment.getTimeSlot().getSlotDate().isAfter(LocalDate.now().minusDays(1)))
                    .toList());
            programs.addAll(programParticipationRepository.findActiveByStudentID(student.getStudentID(), ParticipationStatus.CANCELLED)
                    .stream()
                    .map(p -> new ProgramWithStudents(p.getStudent(), p.getProgram()))
                    .filter(program -> program.getPrograms().getStartDate().isAfter(LocalDate.now().minusDays(1)))
                    .toList());
        }

        if (appointments.isEmpty() && programs.isEmpty()) {
            return eventMapper.buildEmptyEventResponse( new ArrayList<>(), new ArrayList<>(), userId);
        }

        return eventMapper.buildParentEventResponse(appointments, programs, userId);
    }
}

