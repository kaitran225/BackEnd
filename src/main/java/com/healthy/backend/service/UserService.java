package com.healthy.BackEnd.Service;

import com.healthy.BackEnd.DTO.Appointment.AppointmentResponse;
import com.healthy.BackEnd.DTO.Psychologist.PsychologistResponse;
import com.healthy.BackEnd.DTO.Student.StudentResponse;
import com.healthy.BackEnd.DTO.Survey.SurveyQuestionResultResponse;
import com.healthy.BackEnd.DTO.Survey.SurveyResultsResponse;
import com.healthy.BackEnd.DTO.User.UsersResponse;
import com.healthy.BackEnd.Entity.*;
import com.healthy.BackEnd.Exception.ResourceNotFoundException;
import com.healthy.BackEnd.Repository.*;
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
            throw new ResourceNotFoundException("No users found");
        }
        return userRepository.findAllUsers().stream()
                .map(this::convert)
                .toList();
    }

    public UsersResponse getUserById(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
        return convert(user);
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

    public List<AppointmentResponse> getUserAppointments(String userId) {
        return buildAppointmentResponseList(
                userRepository.findById(userId).orElseThrow());
    }

    public List<SurveyResultsResponse> getUserSurveyResults(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return user.getRole() == Users.UserRole.STUDENT
                ? buildSurveyResults(studentRepository.findByUserID(userId).getStudentID())
                : null;
    }

    public void deleteUser(String id) {
        Users user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
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


    private AppointmentResponse buildAppointmentResponse(Users user,Appointments appointment) {
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
                        throw new ResourceNotFoundException("No psychologist found with id " + a.getPsychologistID());
                    }
                    if (s == null) {
                        throw new ResourceNotFoundException("No student found with id " + a.getStudentID());
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
                                    .orElseThrow(() -> new ResourceNotFoundException("No answer found with id " + result.getAnswerID()));
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
                            .orElseThrow(() -> new ResourceNotFoundException("No survey found with id " + entry.getKey()));
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

    private UsersResponse convert_(Users user) {

        Psychologists psychologist = new Psychologists();
        Students student = new Students();
        Parents parent = new Parents();
        if (user.getRole() == Users.UserRole.PSYCHOLOGIST) {
            psychologist = psychologistRepository.findByUserID(user.getUserId());
        }
        if (user.getRole() == Users.UserRole.PARENT) {
            parent = parentRepository.findByUserID(user.getUserId());
        }
        if (user.getRole() == Users.UserRole.STUDENT) {
            student = studentRepository.findByUserID(user.getUserId());
        }
        return UsersResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender().toString())
                .role(user.getRole().toString())
                .psychologistInfo(
                        psychologist.getPsychologistID() == null ? null :
                                PsychologistResponse.builder()
                                        .psychologistId(psychologist.getPsychologistID())
                                        .status(psychologist.getStatus().name())
                                        .specialization(psychologist.getSpecialization())
                                        .yearsOfExperience(psychologist.getYearsOfExperience())
                                        .build()
                )

                .studentInfo(
                        student.getStudentID() == null ? null :
                                StudentResponse.builder()
                                        .studentId(student.getStudentID())
                                        .grade(student.getGrade())
                                        .className(student.getClassName())
                                        .schoolName(student.getSchoolName())
                                        .depressionScore(student.getDepressionScore())
                                        .anxietyScore(student.getAnxietyScore())
                                        .stressScore(student.getStressScore())
                                        .build()
                )
                .children(
                        parent.getParentID() == null ? null :
                                parent.getStudents().isEmpty()
                                        ? Collections.emptyList() :
                                        parent.getStudents().stream().map(s ->
                                                StudentResponse.builder()
                                                        .userId(s.getUserID())
                                                        .studentId(s.getStudentID())
                                                        .grade(s.getGrade())
                                                        .className(s.getClassName())
                                                        .fullName(s.getUser().getFullName())
                                                        .surveyResults(
                                                                surveyResultRepository.findByStudentID(s.getStudentID())
                                                                        .stream()
                                                                        .collect(Collectors.groupingBy(
                                                                                result -> result.getQuestion().getSurveyID(),
                                                                                Collectors.mapping(result -> {
                                                                                    Answers answer = answersRepository.findById(result.getAnswerID())
                                                                                            .orElseThrow(() -> new ResourceNotFoundException("No answer found with id " + result.getAnswerID()));
                                                                                    return SurveyQuestionResultResponse.builder()
                                                                                            .questionId(result.getQuestionID())
                                                                                            .categoryName(String.valueOf(result.getQuestion().getCategory().getCategoryName()))
                                                                                            .questionText(result.getQuestion().getQuestionText())
                                                                                            .resultId(result.getResultID())
                                                                                            .answerId(answer.getAnswerID())
                                                                                            .answer(answer.getAnswer())
                                                                                            .score(answer.getScore())
                                                                                            .build();
                                                                                }, Collectors.toList())))
                                                                        .entrySet()
                                                                        .stream()
                                                                        .map(entry -> {
                                                                            Surveys survey = surveyRepository.findById(entry.getKey())
                                                                                    .orElseThrow(() -> new ResourceNotFoundException("No survey found with id " + entry.getKey()));
                                                                            return SurveyResultsResponse.builder()
                                                                                    .surveyId(survey.getSurveyID())
                                                                                    .surveyName(survey.getSurveyName())
                                                                                    .description(survey.getDescription())
                                                                                    .questions(entry.getValue())
                                                                                    .build();
                                                                        })
                                                                        .toList()
                                                        )
                                                        .build()
                                        ).toList()
                )
                .appointmentsRecord(
                        (psychologist.getPsychologistID() == null && student.getStudentID() == null) ? null :
                                (
                                        user.getRole() == Users.UserRole.STUDENT ?
                                                appointmentRepository.findByStudentID(student.getStudentID()) :
                                                appointmentRepository.findByPsychologistID(psychologist.getPsychologistID())
                                ).stream()
                                        .map(a -> {
                                                    Psychologists p = psychologistRepository.findById(a.getPsychologistID())
                                                            .orElseThrow(() -> new ResourceNotFoundException("No psychologist found with id" + a.getPsychologistID()));
                                                    Students s = studentRepository.findById(a.getStudentID())
                                                            .orElseThrow(() -> new ResourceNotFoundException("No student found with id" + a.getStudentID()));
                                                    return AppointmentResponse.builder()
                                                            .appointmentID(a.getAppointmentID())
                                                            .CreatedAt(a.getCreatedAt())
                                                            .MeetingLink(a.getMeetingLink())
                                                            .Status(a.getStatus().name())
                                                            .psychologistResponse(
                                                                    user.getRole() == Users.UserRole.PSYCHOLOGIST ? null :
                                                                            PsychologistResponse.builder()
                                                                                    .psychologistId(p.getPsychologistID())
                                                                                    .status(p.getStatus().name())
                                                                                    .specialization(p.getSpecialization())
                                                                                    .yearsOfExperience(p.getYearsOfExperience())
                                                                                    .build()
                                                            )
                                                            .studentResponse(
                                                                    user.getRole() == Users.UserRole.STUDENT ? null :
                                                                            StudentResponse.builder()
                                                                                    .studentId(s.getStudentID())
                                                                                    .grade(s.getGrade())
                                                                                    .className(s.getClassName())
                                                                                    .schoolName(s.getSchoolName())
                                                                                    .depressionScore(s.getDepressionScore())
                                                                                    .anxietyScore(s.getAnxietyScore())
                                                                                    .stressScore(s.getStressScore())
                                                                                    .build())
                                                            .Text(a.getNotes())
                                                            .timeSlotID(a.getTimeSlotsID())
                                                            .UpdatedAt(a.getUpdatedAt())
                                                            .build();
                                                }
                                        ).toList()
                )
                .surveyResults(
                        student.getStudentID() == null ? null :
                                surveyResultRepository.findByStudentID(student.getStudentID())
                                        .stream()
                                        .collect(Collectors.groupingBy(
                                                result -> result.getQuestion().getSurveyID(),
                                                Collectors.mapping(result -> {
                                                    Answers answer = answersRepository.findById(result.getAnswerID())
                                                            .orElseThrow(() -> new ResourceNotFoundException("No answer found with id " + result.getAnswerID()));
                                                    return SurveyQuestionResultResponse.builder()
                                                            .questionId(result.getQuestionID())
                                                            .categoryName(String.valueOf(result.getQuestion().getCategory().getCategoryName()))
                                                            .questionText(result.getQuestion().getQuestionText())
                                                            .resultId(result.getResultID())
                                                            .answerId(answer.getAnswerID())
                                                            .answer(answer.getAnswer())
                                                            .score(answer.getScore())
                                                            .build();
                                                }, Collectors.toList())))
                                        .entrySet()
                                        .stream()
                                        .map(entry -> {
                                            Surveys survey = surveyRepository.findById(entry.getKey())
                                                    .orElseThrow(() -> new ResourceNotFoundException("No survey found with id " + entry.getKey()));
                                            return SurveyResultsResponse.builder()
                                                    .surveyId(survey.getSurveyID())
                                                    .surveyName(survey.getSurveyName())
                                                    .description(survey.getDescription())
                                                    .questions(entry.getValue())
                                                    .build();
                                        })
                                        .toList()
                )
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())

                .build();
    }
}