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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    @Autowired
    private StudentRepository studentRepository;

    @Autowired
    private SurveyResultRepository surveyResultRepository;

    @Autowired
    private SurveyRepository surveyRepository;

    @Autowired
    private SurveyQuestionRepository surveyQuestionRepository;

    @Autowired
    private AnswersRepository answersRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PsychologistRepository psychologistRepository;

    @Autowired
    private ParentRepository parentRepository;
    @Autowired
    private AppointmentRepository appointmentRepository;

    public boolean isEmpty() {
        return userRepository.findAll().isEmpty();
    }

    public boolean isUserExist(String id) {
        return userRepository.existsById(id);
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
        if (!isUserExist(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        return convert(Objects.requireNonNull(userRepository.findById(id).orElse(null)));
    }

    public UsersResponse editUser(Users user) {
        UsersResponse existingUser = userRepository.findById(user.getUserId()).map(this::convert)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + user.getUserId()));
        existingUser.setFullName(user.getFullName());
        existingUser.setEmail(user.getEmail());
        existingUser.setPhoneNumber(user.getPhoneNumber());
        existingUser.setRole(String.valueOf(user.getRole()));
        existingUser.setUsername(user.getUsername());
        existingUser.setUpdatedAt(LocalDateTime.now());
        return existingUser;
    }

    public void deleteUser(String id) {
        if (!isUserExist(id)) {
            throw new ResourceNotFoundException("User not found with id: " + id);
        }
        userRepository.deleteById(id);
    }

    public UsersResponse convert(Users user) {

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
