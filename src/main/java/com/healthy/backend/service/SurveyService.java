package com.healthy.backend.service;


import com.healthy.backend.dto.survey.QuestionOption;
import com.healthy.backend.dto.survey.request.ConfirmationRequest;
import com.healthy.backend.dto.survey.request.SurveyRequest;
import com.healthy.backend.dto.survey.request.SurveyUpdateRequest;
import com.healthy.backend.dto.survey.response.*;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.SurveyCategory;
import com.healthy.backend.enums.SurveyStandardType;
import com.healthy.backend.enums.SurveyStatus;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.SurveyMapper;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SurveyService {

    private final SurveyQuestionOptionsChoicesRepository surveyQuestionOptionsChoicesRepository;
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionOptionsRepository surveyQuestionOptionsRepository;
    private final SurveyResultRepository surveyResultRepository;
    private final StudentRepository studentRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final ParentRepository parentRepository;
    private final SurveyMapper surveyMapper;
    private final GeneralService generalService;
    private final SurveyServiceHelper __;

    // Get all survey for display
    public List<SurveysResponse> getAllSurveys(Users user) {
        return switch (user.getRole()) {
            case STUDENT -> {
                Students student = studentRepository.findByUserID(user.getUserId());
                yield getSurveyResult(Set.of(student), student.getStudentID());
            }
            case MANAGER, PSYCHOLOGIST -> {
                List<Surveys> surveys = surveyRepository.findAll();
                yield surveys.stream()
                        .map(survey -> surveyMapper.buildManagerSurveysResponse(
                                survey,
                                getTotalQuestion(survey),
                                getSurveyStatus(survey),
                                getTotalStudentDone(survey) + "/" + getTotalStudent()
                        ))
                        .toList();
            }
            case PARENT -> {
                Parents parent = parentRepository.findByUserID(user.getUserId());
                Set<Students> children = parent.getStudents();
                yield getSurveyResult(children, parent.getParentID());

            }
            default -> throw new RuntimeException("You don't have permission to access this resource.");
        };
    }

    // Get survey questions for taking survey
    public SurveyQuestionResponse getSurveyQuestion(String surveyID) {
        Surveys surveys = surveyRepository.findById(surveyID).orElseThrow(
                () -> new ResourceNotFoundException("No survey found for surveyID " + surveyID));
        List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);
        if (surveyQuestions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for surveyID " + surveyID);
        }
        List<QuestionResponse> questionList = surveyQuestions
                .stream()
                .map(questions -> surveyMapper.buildQuestionResponse(
                        getQuestionResponse(questions),
                        questions,
                        surveyQuestions.indexOf(questions))
                )
                .toList();
        return surveyMapper.buildSurveyQuestionResponse(questionList, surveys);
    }

    public List<SurveyResultsResponse> getSurveyResultsBySurveyID(Users user) {
        List<Surveys> surveyList = surveyRepository.findAll();
        if (surveyList.isEmpty()) {
            return List.of();
        }
        return switch (user.getRole()) {
            case PARENT -> {
                Set<Students> children = parentRepository.findByUserID(user.getUserId()).getStudents();
                yield surveyList.stream()
                        .map(survey -> surveyMapper.mapToListResultsResponse(
                                survey,
                                children.stream()
                                        .flatMap(child -> surveyResultRepository
                                                .findBySurveyIDAndStudentID(survey.getSurveyID(), child.getStudentID()).stream())
                                        .toList().stream()
                                        .map(surveyResult -> getStatusStudent(survey, surveyResult))
                                        .collect(Collectors.toList())
                        ))
                        .collect(Collectors.toList());
            }
            case STUDENT -> {
                yield surveyList.stream()
                        .map(survey -> {
                            List<SurveyResult> surveyResults = surveyResultRepository.findBySurveyIDAndStudentID(survey.getSurveyID(),
                                    studentRepository.findByUserID(user.getUserId()).getStudentID());
                            SurveyResult surveyResult = surveyResults.isEmpty() ? null : surveyResults.getLast();
                            if (surveyResult == null) {
                                return surveyMapper.mapToListResultsResponse(survey, List.of());
                            } else {
                                return surveyMapper.mapToListResultsResponse(
                                        survey,
                                        List.of(getStatusStudent(survey, surveyResult)));
                            }
                        })
                        .collect(Collectors.toList());
            }
            case MANAGER, PSYCHOLOGIST -> {
                yield surveyList.stream()
                        .map(survey -> {
                            List<SurveyResult> surveyResultList = surveyResultRepository.findBySurveyID(survey.getSurveyID());

                            return surveyMapper.mapToListResultsResponse(
                                    survey,
                                    surveyResultList.stream()
                                            .map(surveyResult -> getStatusStudent(survey, surveyResult))
                                            .collect(Collectors.toList())
                            );
                        })
                        .collect(Collectors.toList());
            }
            default -> throw new RuntimeException("You don't have permission to access");
        };
    }

    private StatusStudentResponse getStatusStudent(Surveys survey, SurveyResult surveyResult) {
        return surveyMapper.mapToResultStudent(
                getStatusStudent(survey.getSurveyID(), surveyResult.getStudentID()),
                surveyResult.getResult() + "/" + surveyResult.getMaxScore(),
                surveyResult.getStudentID());
    }

    public SurveyQuestionResponse getSurveyResultByStudentID(String surveyID, String studentId) {

        if (!surveyResultRepository.existsBySurveyIDAndStudentID(surveyID, studentId)) {
            throw new ResourceNotFoundException("No survey found for surveyID " + surveyID + " with studentID " + studentId);
        }

        Surveys survey = surveyRepository.findById(surveyID).orElseThrow(
                () -> new ResourceNotFoundException("No survey found for surveyID " + surveyID));
        List<SurveyResult> surveyResult = surveyResultRepository.findBySurveyIDAndStudentID(surveyID, studentId);
        List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);
        if (surveyQuestions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for surveyID " + surveyID);
        }
        List<QuestionResponse> questionList = surveyQuestions
                .stream()
                .map(questions -> {
                            List<QuestionOption> questionOption = getQuestionResponse(questions);
                            questionOption.forEach(option -> {
                                if (option.getAnswerID().equals(surveyResult.getLast().getChoices().get(surveyQuestions.indexOf(questions)).getOptionID())) {
                                    option.setChecked(true);
                                }
                            });
                            return surveyMapper.buildQuestionResponse(
                                    questionOption,
                                    questions,
                                    surveyQuestions.indexOf(questions));
                        }
                )
                .toList();
        return surveyMapper.buildSurveyResultResponse(questionList,
                survey,
                getStatusStudent(survey.getSurveyID(), studentId),
                surveyResult.getLast().getResult() + "/" + surveyResult.getLast().getMaxScore());
    }

    public void updateSurvey(String surveyID, SurveyUpdateRequest surveyUpdateRequest) {

    }

    public void createSurvey(SurveyRequest surveyRequest, Users users) {

    }

    // Deactivate
    public void deactivateSurvey(String surveyId) {
        Surveys survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey Not found"));
        survey.setStatus(SurveyStatus.INACTIVE);
        surveyRepository.save(survey);
    }

    // Activate
    public void activateSurvey(String surveyId) {
        Surveys survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey Not found"));
        survey.setStatus(SurveyStatus.ACTIVE);
        surveyRepository.save(survey);
    }

    public StatusStudentResponse getScoreFromStudentInSuv(String surveyId, List<String> optionId, String studentId) {
        StatusStudentResponse status = new StatusStudentResponse();
        Surveys survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found survey"));
        List<SurveyQuestions> surveyQuestion = surveyQuestionRepository.findBySurveyID(survey.getSurveyID());
        int total = calScore(surveyQuestion, optionId);
        int max = calMaxScore(survey.getStandardType(), surveyQuestion.size());
        SurveyResult saveResult = surveyResultRepository.save(
                new SurveyResult(generalService.generateSurveyResultId(), surveyId, studentId, total, max)
        );
        saveAvgScore(survey, total, studentRepository.findByStudentID(studentId));
        if (saveResult.getResultID() != null) {
            saveSurveyOptionsChoice(saveResult.getResultID(), optionId);
            status = surveyMapper.mapToResultStudent(total + "/" + max, studentId);
        }
        if (status == null) {
            return surveyMapper.mapToResultStudent("0", "NOT FINISHED", studentId);
        }
        return status;
    }

    private void saveSurveyOptionsChoice(String resultID, List<String> optionId) {
        List<SurveyQuestionOptionsChoices> choiceList = new ArrayList<>();
        for (String surveyQuestionOption : optionId) {
            SurveyQuestionOptions question = surveyQuestionOptionsRepository.findByOptionID(surveyQuestionOption);
            String questionId = question.getQuestionID();

            SurveyQuestionOptionsChoices sqc = new SurveyQuestionOptionsChoices(resultID, questionId, surveyQuestionOption);
            choiceList.add(sqc);
        }
        surveyQuestionOptionsChoicesRepository.saveAll(choiceList);
    }

    private List<SurveysResponse> getSurveyResult(Set<Students> students, String ID) {
        List<Surveys> surveyList = surveyRepository.findAll();
        return surveyList.stream()
                .map(survey -> {
                    List<StatusStudentResponse> statusStuList = students.stream()
                            .map(student -> {

                                List<SurveyResult> surveyResultSTD = surveyResultRepository.findBySurveyIDAndStudentID(
                                        survey.getSurveyID(),
                                        student.getStudentID());

                                if (surveyResultSTD.isEmpty()) {
                                    return List.of(surveyMapper.mapToResultStudent(
                                                    "0/0",
                                                    student.getStudentID()
                                            )
                                    );
                                }

                                return surveyResultSTD.stream()
                                        .map(result -> {
                                            return surveyMapper.mapToResultStudent(
                                                    result.getResult() + "/" + result.getMaxScore(),
                                                    student.getStudentID()
                                            );
                                        })
                                        .collect(Collectors.toList());
                            })
                            .flatMap(List::stream)
                            .collect(Collectors.toList());

                    return surveyMapper.buildSurveysResponse1(survey,
                            getTotalQuestion(survey),
                            ID.contains("PAR") ? null : getStatusStudent(survey.getSurveyID(), ID),
                            statusStuList,
                            (survey.isSurveyOpen() || !areAllStudentsCompleted(survey)) ? "ACTIVE" : "INACTIVE");

                })
                .collect(Collectors.toList());
    }

    public int calculateTotalScore(SurveyResult result) {
        return calScore(surveyQuestionRepository.findBySurveyID(result.getSurveyID()),
                result.getChoices().stream().map(
                        SurveyQuestionOptionsChoices::getOptionsChoicesID
                ).toList());
    }

    public int calculateMaxScore(Surveys surveys) {
        return calMaxScore(surveys.getStandardType(),
                surveyQuestionRepository.countQuestionInSuv(surveys.getSurveyID()));
    }

    private int getTotalQuestion(Surveys surveys) {
        return (int) surveyQuestionRepository.countBySurveyID(surveys.getSurveyID());
    }

    private int getTotalStudentDone(Surveys surveys) {
        return surveyResultRepository.countDistinctStudentsBySurveyID(surveys.getSurveyID());
    }

    private int getTotalStudent() {
        return (int) studentRepository.count();
    }

    private String getSurveyStatus(Surveys surveys) {
        return getTotalStudent() == getTotalStudentDone(surveys) ? "COMPLETED" : "NOT COMPLETED";
    }

    private String getStatusStudent(String surveyId, String studentId) {
        return !surveyResultRepository.existsBySurveyIDAndStudentID(surveyId, studentId)
                ? "NOT COMPLETED"
                : "COMPLETED";
    }

    private List<QuestionOption> getQuestionResponse(SurveyQuestions questions) {
        return surveyQuestionOptionsRepository
                .findByQuestionID(questions.getQuestionID())
                .stream()
                .map(surveyMapper::buildNewQuestionOption)
                .toList();
    }

    public ConfirmationRequest getLowScoringStudentsForAppointment(Users users) {
        Students students = studentRepository.findByUserID(users.getUserId());
        List<SurveyResult> results = surveyResultRepository.findByStudentID(students.getStudentID());

        if (results.isEmpty()) {
            return null;
        }

        SurveyResult lastResult = results.getLast();

        if (lastResult.getResult() == 0 || lastResult.getResult() > (0.95 * lastResult.getMaxScore())) {
            return new ConfirmationRequest(students.getStudentID(), false);
        }
        return new ConfirmationRequest(students.getStudentID(), true);
    }

    public boolean handleAppointmentRequest(ConfirmationRequest request) {
        return request.isConfirmation();
    }

    private void saveAvgScore(Surveys survey, int result, Students student) {
        int size = surveyResultRepository.countResultStudent(survey.getSurveyID(), student.getStudentID());
        switch (survey.getStandardType()) {
            case PSS_10:
                student.setStressScore(__.calculateNewWeightedAvg(
                        student.getStressScore(),
                        result,
                        size));
                break;
            case GAD_7:
                student.setAnxietyScore(__.calculateNewWeightedAvg(
                        student.getAnxietyScore(),
                        result,
                        size));
                break;
            case PHQ_9:
                student.setDepressionScore(__.calculateNewWeightedAvg(
                        student.getDepressionScore(),
                        result,
                        size));
                break;
            default:
                throw new AssertionError("Invalid standard type");
        }
        studentRepository.save(student);
    }

    private int calMaxScore(SurveyStandardType standardType, int size) {
        switch (standardType) {
            case PSS_10 -> {
                return size * 4;
            }
            case GAD_7, PHQ_9 -> {
                return size * 3;
            }
            default -> {
                throw new IllegalArgumentException("Invalid Standard Type: " + standardType);
            }
        }
    }

    private int calScore(List<SurveyQuestions> questions, List<String> choices) {
        List<SurveyQuestionOptions> surveyOptions = new ArrayList<>();
        for (SurveyQuestions question : questions) {
            surveyOptions.addAll(surveyQuestionOptionsRepository.findByQuestionID(question.getQuestionID()));
        }
        return surveyOptions.stream()
                .filter(option -> choices.contains(option.getOptionID()))
                .mapToInt(SurveyQuestionOptions::getScore)
                .sum();
    }

    public boolean hasStudentCompletedSurveyThisMonth(String studentId, String surveyId) {
        LocalDate now = LocalDate.now();
        List<SurveyResult> completions = surveyResultRepository.findBySurveyIDAndStudentID(surveyId, studentId);

        if (completions.isEmpty()) {
            return false;
        }

        return completions.stream()
                .anyMatch(completion ->
                        completion.getCompletionDate().getMonth() == now.getMonth() &&
                                completion.getCompletionDate().getYear() == now.getYear()
                );
    }

    public List<SurveyStandardType> getAllStandardTypes() {
        return Arrays.asList(SurveyStandardType.values());
    }

    public List<SurveyCategory> getAllCategories() {
        return Arrays.asList(SurveyCategory.values());
    }

    public boolean areAllStudentsCompleted(Surveys survey) {
        List<Students> studentList = studentRepository.findAll();
        for (Students student : studentList) {
            if (!hasStudentCompletedSurveyThisMonth(student.getStudentID(), survey.getSurveyID())) {
                return false;
            }
        }
        return true;
    }
}