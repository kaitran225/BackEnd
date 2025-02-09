package com.healthy.backend.service;

import com.healthy.backend.dto.appointment.AppointmentResponse;
import com.healthy.backend.dto.psychologist.PsychologistResponse;
import com.healthy.backend.dto.student.StudentResponse;
import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.user.UsersResponse;
import com.healthy.backend.entity.*;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final StudentRepository studentRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final SurveyRepository surveyRepository;
    private final AnswersRepository answersRepository;
    private final UserRepository userRepository;
    private final PsychologistRepository psychologistRepository;
    private final ParentRepository parentRepository;
    private final AppointmentRepository appointmentRepository;

    public boolean isEmpty() {
        return userRepository.findAll().isEmpty();
    }

    public List<UsersResponse> getAllUsers() {
        if (isEmpty()) {
            throw new RuntimeException("No users found");
        }
        return userRepository.findAllUsers().stream()
                .map(this::convert)
                .collect(Collectors.toList());
    }

    public UsersResponse getUserById(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        return convert(user);
    }

    public UsersResponse updateUser(String userId, Users updatedUser) {
        Users existingUser = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId)); // APIExceptionHandle will catch this

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

    public List<AppointmentResponse> getUserAppointments(String userId) {
        return buildAppointmentResponseList(
                userRepository.findById(userId).orElseThrow());
    }

    public List<SurveyResultsResponse> getUserSurveyResults(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
        return user.getRole() == Users.UserRole.STUDENT
                ? buildSurveyResults(studentRepository.findByUserID(userId).getStudentID())
                : null;
    }

    public void deleteUser(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + id));
        userRepository.delete(user);
    }

    ////////////// Converters //////////////
    private PsychologistResponse buildPsychologistResponse(Users user) {
        Psychologists psychologist = psychologistRepository.findByUserID(user.getUserId());
        if (psychologist == null || psychologist.getPsychologistID() == null)
            return null;
        return PsychologistResponse.builder()
                .psychologistId(psychologist.getPsychologistID())
                .status(psychologist.getStatus().name())
                .specialization(psychologist.getSpecialization())
                .yearsOfExperience(psychologist.getYearsOfExperience())
                .build();
    }

    private StudentResponse buildStudentResponse(Students student) {
        if (student == null || student.getStudentID() == null)
            return null;
        return StudentResponse.builder()
                .studentId(student.getStudentID())
                .grade(student.getGrade())
                .className(student.getClassName())
                .schoolName(student.getSchoolName())
                .depressionScore(student.getDepressionScore())
                .anxietyScore(student.getAnxietyScore())
                .stressScore(student.getStressScore())
                .surveyResults(buildSurveyResults(student.getStudentID()))
                .build();
    }


    private AppointmentResponse buildAppointmentResponse(Users user, Appointments appointment) {
        return AppointmentResponse.builder()
                .appointmentID(appointment.getAppointmentID())
                .CreatedAt(appointment.getCreatedAt())
                .MeetingLink(appointment.getMeetingLink())
                .Status(appointment.getStatus().name())
                .psychologistResponse(
                        user.getRole() == Users.UserRole.PSYCHOLOGIST ? null :
                                buildPsychologistResponse(user)
                )
                .studentResponse(
                        user.getRole() == Users.UserRole.STUDENT ? null :
                                buildStudentResponse(studentRepository.findByUserID(user.getUserId()))
                )
                .Text(appointment.getNotes())
                .timeSlotID(appointment.getTimeSlotsID())
                .UpdatedAt(appointment.getUpdatedAt())
                .build();
    }

    private List<AppointmentResponse> buildAppointmentResponseList(Users user) {

        Psychologists psychologist = psychologistRepository.findByUserID(user.getUserId());
        Students student = studentRepository.findByUserID(user.getUserId());
        if (psychologist == null && student == null) {
            return null;
        }

        List<Appointments> appointments = (user.getRole() == Users.UserRole.STUDENT)
                ? appointmentRepository.findByStudentID(student.getStudentID())
                : appointmentRepository.findByPsychologistID(psychologist.getPsychologistID());

        Map<String, Psychologists> psychologistMap = psychologistRepository.findAll().stream()
                .collect(Collectors.toMap(Psychologists::getPsychologistID, p -> p));
        Map<String, Students> studentMap = studentRepository.findAll().stream()
                .collect(Collectors.toMap(Students::getStudentID, s -> s));
        return appointments.stream()
                .map(a -> {
                    Psychologists p = psychologistMap.get(a.getPsychologistID());
                    Students s = studentMap.get(a.getStudentID());
                    if (p == null) {
                        throw new RuntimeException("No psychologist found with id " + a.getPsychologistID());
                    }
                    if (s == null) {
                        throw new RuntimeException("No student found with id " + a.getStudentID());
                    }
                    return buildAppointmentResponse(user, a);
                })
                .toList();
    }

    private List<SurveyResultsResponse> buildSurveyResults(String studentId) {
        return surveyResultRepository.findByStudentID(studentId)
                .stream()
                .collect(Collectors.groupingBy(result -> result.getQuestion().getSurveyID(),
                        Collectors.mapping(result -> {
                            Answers answer = answersRepository.findById(result.getAnswerID())
                                    .orElseThrow(() -> new RuntimeException("No answer found with id " + result.getAnswerID()));
                            return SurveyQuestionResultResponse.builder()
                                    .questionId(result.getQuestionID())
                                    .categoryName(String.valueOf(result.getQuestion().getCategory().getCategoryName()))
                                    .questionText(result.getQuestion().getQuestionText())
                                    .resultId(result.getResultID())
                                    .answerId(answer.getAnswerID())
                                    .answer(answer.getAnswer())
                                    .score(answer.getScore())
                                    .build();
                        }, Collectors.toList()))).entrySet()
                .stream()
                .map(entry -> {
                    Surveys survey = surveyRepository.findById(entry.getKey())
                            .orElseThrow(() -> new RuntimeException("No survey found with id " + entry.getKey()));
                    return SurveyResultsResponse.builder()
                            .surveyId(survey.getSurveyID())
                            .surveyName(survey.getSurveyName())
                            .description(survey.getDescription())
                            .questions(entry.getValue())
                            .build();
                })
                .toList();
    }

    private UsersResponse convert(Users user) {
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .role(user.getRole().toString())
                .psychologistInfo(buildPsychologistResponse(user))
                .studentInfo(buildStudentResponse(studentRepository.findByUserID(user.getUserId())))
                .children(
                        user.getRole() == Users.UserRole.PARENT ?
                                parentRepository.findByUserID(user.getUserId()).getStudents()
                                        .stream()
                                        .map(this::buildStudentResponse)
                                        .collect(Collectors.toList()) : null
                )
                .appointmentsRecord(
                        buildAppointmentResponseList(user)
                )
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();
    }
}