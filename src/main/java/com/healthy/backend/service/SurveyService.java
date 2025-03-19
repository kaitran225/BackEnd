package com.healthy.backend.service;


import com.healthy.backend.dto.survey.QuestionOption;
import com.healthy.backend.dto.survey.request.*;
import com.healthy.backend.dto.survey.response.*;
import com.healthy.backend.entity.*;
import com.healthy.backend.enums.Role;
import com.healthy.backend.enums.SurveyCategory;
import com.healthy.backend.enums.SurveyStandardType;
import com.healthy.backend.enums.SurveyStatus;
import com.healthy.backend.exception.OperationFailedException;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.SurveyMapper;
import com.healthy.backend.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final PeriodicRepository periodicRepository;
    private final SurveyMapper surveyMapper;
    private final GeneralService generalService;
    private final SurveyServiceHelper __;


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


    // Get all survey for display
    public List<SurveysResponse> getAllSurveys(Users user) {
        return switch (user.getRole()) {
            case STUDENT -> {
                Students student = studentRepository.findByUserID(user.getUserId());
                yield getSurveyResult(student, student.getStudentID());
            }
            case MANAGER, PSYCHOLOGIST -> {
                List<Surveys> surveys = surveyRepository.findAll();
                yield surveys.stream()
                        .map(survey -> surveyMapper.buildManagerSurveysResponse(
                                survey,
                                getTotalQuestion(survey),
                                getSurveyStatus(survey),
                                getTotalStudentDoneInCurrentPeriod(survey) + "/" + getTotalStudent()
                        ))
                        .toList();
            }
            case PARENT -> {
                Parents parent = parentRepository.findByUserID(user.getUserId());
                Set<Students> children = parent.getStudents();
                yield getChildrenSurveyResult(children, parent.getParentID());

            }
            default -> throw new RuntimeException("You don't have permission to access this resource.");
        };
    }

    // Get survey questions for taking survey
    public SurveyQuestionResponse getSurveyQuestion(String surveyID, Users users) {
        Surveys surveys = surveyRepository.findById(surveyID).orElseThrow(
                () -> new ResourceNotFoundException("No survey found for surveyID " + surveyID));

        if (users.getRole().equals(Role.STUDENT)) {
            Students student = studentRepository.findByUserID(users.getUserId());
            // Check if student have done this survey before
            if (hasDone(surveys, studentRepository.findByStudentID(student.getStudentID()))) {
                throw new OperationFailedException("You have done this survey before");
            }

            if (!isInSession(surveys)) {
                throw new OperationFailedException("This survey is not in session");
            }
        }

        List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);
        if (surveyQuestions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for surveyID " + surveyID);
        }
        List<QuestionResponse> questionList = surveyQuestions
                .stream()
                .map(questions -> surveyMapper.buildQuestionResponse(
                        getQuestionResponse(questions),
                        questions,
                        surveyQuestions.indexOf(questions), users.getRole())
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
            case STUDENT -> surveyList.stream()
                    .map(survey -> {
                        List<SurveyResult> surveyResults = surveyResultRepository.findBySurveyIDAndStudentID(survey.getSurveyID(),
                                studentRepository.findByUserID(user.getUserId()).getStudentID());
                        SurveyResult surveyResult = surveyResults.isEmpty() ? null : surveyResults.getLast();

                        if (surveyResult == null) {
                            return surveyMapper.mapToListResultsResponse(survey, List.of());
                        }

                        if (surveyResult.getCompletionDate().isBefore(survey.getStartDate())) {
                            return surveyMapper.mapToListResultsResponse(survey, List.of());
                        }

                        return surveyMapper.mapToListResultsResponse(
                                survey,
                                List.of(getStatusStudent(survey, surveyResult)));

                    })
                    .collect(Collectors.toList());
            case MANAGER, PSYCHOLOGIST -> surveyList.stream()
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
            default -> throw new RuntimeException("You don't have permission to access");
        };
    }

    public List<SurveyQuestionResponse> getAllSurveyResultByStudentID(String surveyID, String studentId) {

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

        return surveyResult.stream()
                .map(result -> {
                    List<QuestionResponse> questionList = surveyQuestions
                            .stream()
                            .map(questions -> {
                                        List<QuestionOption> questionOption = getQuestionResponse(questions);
                                        questionOption.forEach(option -> {
                                            if (option.getAnswerID().equals(
                                                    surveyResult.getLast().getChoices().get(
                                                            surveyQuestions.indexOf(questions)).getOptionID())
                                            ) {
                                                option.setChecked(true);
                                            }
                                        });
                                        return surveyMapper.buildQuestionResponse(
                                                questionOption,
                                                questions,
                                                surveyQuestions.indexOf(questions),
                                                Role.STUDENT
                                        );
                                    }
                            )
                            .toList();
                    return surveyMapper.buildSurveyResultResponse(questionList,
                            survey,
                            getStatusStudent(survey.getSurveyID(), studentId),
                            result.getResult() + "/" + result.getMaxScore());
                })
                .collect(Collectors.toList());

    }

    public SurveyQuestionResponse getSurveyResultByStudentID(String surveyID, String studentId) {

        if (!surveyResultRepository.existsBySurveyIDAndStudentID(surveyID, studentId)) {
            throw new ResourceNotFoundException("No survey found for surveyID " + surveyID + " with studentID " + studentId);
        }

        Surveys survey = surveyRepository.findById(surveyID).orElseThrow(
                () -> new ResourceNotFoundException("No survey found for surveyID " + surveyID));
        SurveyResult surveyResult = surveyResultRepository.findBySurveyIDAndStudentID(surveyID, studentId).getLast();

        if (surveyResult.getCompletionDate().isBefore(survey.getStartDate())) {
            throw new OperationFailedException("You have not done this survey for this period yet");
        }

        List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);
        if (surveyQuestions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for surveyID " + surveyID);
        }
        List<QuestionResponse> questionList = surveyQuestions
                .stream()
                .map(questions -> {
                            List<QuestionOption> questionOption = getQuestionResponse(questions);
                            questionOption.forEach(option -> {
                                if (option.getAnswerID().equals(
                                        surveyResult.getChoices().get(
                                                surveyQuestions.indexOf(questions)).getOptionID())
                                ) {
                                    option.setChecked(true);
                                }
                            });
                            return surveyMapper.buildQuestionResponse(
                                    questionOption,
                                    questions,
                                    surveyQuestions.indexOf(questions),
                                    Role.STUDENT
                            );
                        }
                )
                .toList();
        return surveyMapper.buildSurveyResultResponse(questionList,
                survey,
                getStatusStudent(survey.getSurveyID(), studentId),
                surveyResult.getResult() + "/" + surveyResult.getMaxScore());
    }

    private void validateSurveyUpdateRequest(SurveyUpdateRequest surveyUpdateRequest) {
        if (surveyUpdateRequest == null) {
            throw new IllegalArgumentException("Survey update request cannot be null.");
        }
        if (surveyUpdateRequest.getStartDate() != null) {
            try {
                LocalDate.parse(surveyUpdateRequest.getStartDate());
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
            }
        }
    }

    @Transactional
    private void updateSurveyQuestions(Surveys existingSurvey, List<QuestionUpdateRequest> questionRequests) {
        List<SurveyQuestions> existingQuestions = surveyQuestionRepository.findBySurveyID(existingSurvey.getSurveyID());

        // Convert existing questions to a map for quick lookup
        Map<String, SurveyQuestions> existingQuestionMap = existingQuestions.stream()
                .collect(Collectors.toMap(SurveyQuestions::getQuestionID, q -> q));

        List<SurveyQuestions> questionsToSave = new ArrayList<>();
        List<SurveyQuestionOptions> optionsToSave = new ArrayList<>();
        Set<String> updatedQuestionIds = new HashSet<>();

        for (QuestionUpdateRequest questionRequest : questionRequests) {
            String questionText = (questionRequest.getQuestionText() != null) ? questionRequest.getQuestionText().trim() : null;
            if (questionRequest.getQuestionId() != null && existingQuestionMap.containsKey(questionRequest.getQuestionId())) {
                // Update existing question
                SurveyQuestions existingQuestion = existingQuestionMap.get(questionRequest.getQuestionId());
                if (questionText != null) {
                    existingQuestion.setQuestionText(questionText);
                }
                questionsToSave.add(existingQuestion);
                updatedQuestionIds.add(existingQuestion.getQuestionID());
            } else {
                // New question
                SurveyQuestions newQuestion = new SurveyQuestions(
                        generalService.generateSurveyQuestionId(),
                        existingSurvey.getSurveyID(),
                        questionText
                );
                questionsToSave.add(newQuestion);
                existingQuestionMap.put(newQuestion.getQuestionID(), newQuestion);
            }
        }

        surveyQuestionRepository.saveAll(questionsToSave);
        updateSurveyOptions(existingQuestionMap, questionRequests, optionsToSave);
        surveyQuestionOptionsRepository.saveAll(optionsToSave);

        // Identify removed questions and delete related options
        List<String> removedQuestionIds = existingQuestions.stream()
                .map(SurveyQuestions::getQuestionID)
                .filter(id -> !updatedQuestionIds.contains(id))
                .toList();

        if (!removedQuestionIds.isEmpty()) {
            surveyQuestionOptionsRepository.deleteByQuestionIds(removedQuestionIds);
            surveyQuestionRepository.deleteAllByIdInBatch(removedQuestionIds);
        }
    }


    private void updateSurveyOptions(Map<String, SurveyQuestions> existingQuestionMap,
                                     List<QuestionUpdateRequest> questionRequests,
                                     List<SurveyQuestionOptions> optionsToSave) {
        for (QuestionUpdateRequest questionRequest : questionRequests) {
            SurveyQuestions question = existingQuestionMap.get(questionRequest.getQuestionId());
            if (question == null) continue;

            List<SurveyQuestionOptions> existingOptions = surveyQuestionOptionsRepository.findByQuestionID(question.getQuestionID());
            Map<String, SurveyQuestionOptions> existingOptionsMap = existingOptions.stream()
                    .collect(Collectors.toMap(SurveyQuestionOptions::getOptionID, o -> o));

            Set<String> updatedOptionIds = new HashSet<>();

            for (QuestionOptionUpdateRequest optionRequest : questionRequest.getQuestionOptions()) {
                if (optionRequest.getOptionID() != null && existingOptionsMap.containsKey(optionRequest.getOptionID())) {
                    // Update existing option
                    SurveyQuestionOptions existingOption = existingOptionsMap.get(optionRequest.getOptionID());
                    if (optionRequest.getLabel() != null) {
                        existingOption.setOptionText(optionRequest.getLabel().trim());
                    }
                    existingOption.setScore(optionRequest.getValue());
                    optionsToSave.add(existingOption);
                    updatedOptionIds.add(existingOption.getOptionID());
                } else {
                    // New option
                    SurveyQuestionOptions newOption = new SurveyQuestionOptions(
                            generalService.generateQuestionOptionId(),
                            question.getQuestionID(),
                            optionRequest.getLabel().trim(),
                            optionRequest.getValue()
                    );
                    optionsToSave.add(newOption);
                }
            }

            // Delete removed options
            List<String> removedOptionIds = existingOptions.stream()
                    .map(SurveyQuestionOptions::getOptionID)
                    .filter(id -> !updatedOptionIds.contains(id))
                    .toList();

            if (!removedOptionIds.isEmpty()) {
                surveyQuestionOptionsRepository.deleteAllById(removedOptionIds);
            }
        }
    }

    public void updateSurveyQuestion(String surveyID, List<QuestionUpdateRequest> questionRequest) {
        Surveys existingSurvey = surveyRepository.findById(surveyID)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + surveyID));
        updateSurveyQuestions(existingSurvey, questionRequest);
    }

    @Transactional
    public void updateSurvey(String surveyID, SurveyUpdateRequest surveyUpdateRequest) {

        Surveys existingSurvey = surveyRepository.findById(surveyID)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found with ID: " + surveyID));

        validateSurveyUpdateRequest(surveyUpdateRequest);

        if (surveyUpdateRequest.getTitle() != null) {
            existingSurvey.setSurveyName(surveyUpdateRequest.getTitle().trim());
        }
        if (surveyUpdateRequest.getDescription() != null) {
            existingSurvey.setDescription(surveyUpdateRequest.getDescription().trim());
        }
        if (surveyUpdateRequest.getStartDate() != null) {
            try {
                existingSurvey.setStartDate(LocalDate.parse(surveyUpdateRequest.getStartDate()).atStartOfDay());
            } catch (DateTimeParseException e) {
                throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
            }
        }
        if (surveyUpdateRequest.getStandType() != null) {
            try {
                existingSurvey.setStandardType(SurveyStandardType.valueOf(surveyUpdateRequest.getStandType().toUpperCase()));
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid survey standard type: " + surveyUpdateRequest.getStandType());
            }
        }
        if (surveyUpdateRequest.getPeriodic() > 0) {
            existingSurvey.setDuration(surveyUpdateRequest.getPeriodic());
        }
        if (surveyUpdateRequest.getStatus() != null) {
            existingSurvey.setStatus(SurveyStatus.valueOf(surveyUpdateRequest.getStatus().toUpperCase()));
        }
        Surveys surveys = surveyRepository.save(existingSurvey);
        periodicRepository.save(new Periodic(createPeriodicID(surveys), surveys, surveys.getStartDate(), surveys.getEndDate()));
    }

    private String createPeriodicID(Surveys surveys) {
        String periodicSize;
        if (surveys.getPeriodic() == null) {
            periodicSize = String.format("%03d", 0);
            return surveys.getSurveyID() + "_" + periodicSize;
        }
        ;
        periodicSize = String.format("%03d", surveys.getPeriodic().size());
        return surveys.getSurveyID() + "_" + periodicSize;
    }

    // Create survey
    @Transactional
    public void createSurvey(SurveyRequest surveyRequest, Users users) {
        validateSurvey(surveyRequest);
        Surveys surveys = saveSurvey(surveyRequest, users);
        periodicRepository.save(new Periodic(createPeriodicID(surveys), surveys, surveys.getStartDate(), surveys.getEndDate()));
        saveSurveyQuestionsAndOptions(surveyRequest.getQuestion(), surveys);
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


    public List<SurveyStandardType> getAllStandardTypes() {
        return Arrays.asList(SurveyStandardType.values());
    }

    public List<SurveyCategory> getAllCategories() {
        return Arrays.asList(SurveyCategory.values());
    }

    public boolean handleAppointmentRequest(ConfirmationRequest request) {
        return request.isConfirmation();
    }

    private boolean hasDone(Surveys survey, Students students) {
        List<SurveyResult> results = surveyResultRepository.findBySurveyIDAndStudentID(
                survey.getSurveyID(), students.getStudentID()
        );
        if (results.isEmpty()) {
            return false;
        }
        SurveyResult latestResult = results.getLast();
        if (latestResult == null) {
            return false;
        }
        if (latestResult.getIsRepeat()) {
            return false;
        }
        LocalDateTime latestCompleteDate = latestResult.getCompletionDate();
        LocalDateTime startDate = survey.getStartDate();
        LocalDateTime endDate = survey.getEndDate();
        if (latestCompleteDate != null && startDate != null && endDate != null) {
            return !latestCompleteDate.isBefore(startDate) && !latestCompleteDate.isAfter(endDate);
        }
        return false;
    }

    private boolean isInSession(Surveys survey) {
        LocalDateTime today = LocalDateTime.now();
        LocalDateTime startDate = survey.getStartDate();
        LocalDateTime endDate = survey.getEndDate();
        if (startDate != null && endDate != null) {
            return !today.isBefore(startDate) && !today.isAfter(endDate);
        }
        return false;
    }

    public StatusStudentResponse submitSurvey(String surveyId, List<String> optionId, String studentId) {
        Surveys survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Not found survey"));

        // Check if student have done this survey before
        if (hasDone(survey, studentRepository.findByStudentID(studentId))) {
            throw new OperationFailedException("You have done this survey before");
        }

        if (!isInSession(survey)) {
            throw new OperationFailedException("This survey is not in session");
        }

        List<SurveyQuestions> surveyQuestion = surveyQuestionRepository.findBySurveyID(survey.getSurveyID());

        if (surveyQuestion.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for surveyID " + surveyId);
        }

        if (optionId.size() != surveyQuestion.size()) {
            throw new OperationFailedException("Number of options does not match number of questions");
        }

        // Calculate scores
        int total = calScore(surveyQuestion, optionId);
        int max = calMaxScore(survey.getStandardType(), surveyQuestion.size());

        // Save result
        SurveyResult saveResult = surveyResultRepository.save(
                new SurveyResult(generalService.generateSurveyResultId(), surveyId, studentId, total, max)
        );

        // Set new average score
        saveAvgScore(survey, total, studentRepository.findByStudentID(studentId));


        if (saveResult.getResultID() != null) {
            saveSurveyOptionsChoice(saveResult.getResultID(), optionId);
            return surveyMapper.mapToResultStudent(total + "/" + max, studentId, saveResult.getCompletionDate());
        }

        return surveyMapper.mapToResultStudent("0", "NOT FINISHED", studentId, saveResult.getCompletionDate());
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////// Private //////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////

    private void validateSurvey(SurveyRequest surveyRequest) {
        if (surveyRequest == null) {
            throw new IllegalArgumentException("Survey request cannot be null.");
        }
        if (surveyRequest.getTitle() == null || surveyRequest.getTitle().trim().isEmpty()) {
            throw new IllegalArgumentException("Survey title cannot be empty.");
        }
        if (surveyRequest.getDescription() == null || surveyRequest.getDescription().trim().isEmpty()) {
            throw new IllegalArgumentException("Survey description cannot be empty.");
        }
        if (surveyRequest.getStartDate() == null || surveyRequest.getStartDate().trim().isEmpty()) {
            throw new IllegalArgumentException("Survey start date is required.");
        }
        if (surveyRequest.getStandardType() == null || surveyRequest.getStandardType().trim().isEmpty()) {
            throw new IllegalArgumentException("Survey standard type is required.");
        }

        // Validate question list
        if (surveyRequest.getQuestion() == null || surveyRequest.getQuestion().isEmpty()) {
            throw new IllegalArgumentException("Survey must contain at least one question.");
        }

        for (QuestionRequest question : surveyRequest.getQuestion()) {
            if (question.getQuestionText() == null || question.getQuestionText().trim().isEmpty()) {
                throw new IllegalArgumentException("Each question must have text.");
            }
            if (question.getQuestionOptions() == null || question.getQuestionOptions().isEmpty()) {
                throw new IllegalArgumentException("Each question must have at least one option.");
            }
            for (QuestionOptionRequest option : question.getQuestionOptions()) {
                if (option.getLabel() == null || option.getLabel().trim().isEmpty()) {
                    throw new IllegalArgumentException("Option label cannot be empty.");
                }
            }
        }
    }

    private Surveys saveSurvey(SurveyRequest surveyRequest, Users users) {
        LocalDate startDate;
        SurveyStandardType standardType;

        try {
            startDate = LocalDate.parse(surveyRequest.getStartDate());
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException("Invalid date format. Expected format: yyyy-MM-dd");
        }

        try {
            standardType = SurveyStandardType.valueOf(surveyRequest.getStandardType().toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Invalid survey standard type: " + surveyRequest.getStandardType());
        }

        return surveyRepository.save(new Surveys(
                generalService.generateSurveyID(),
                surveyRequest.getTitle().trim(),
                surveyRequest.getDescription().trim(),
                users.getUserId(),
                startDate,
                surveyRequest.getPeriodic(),
                standardType
        ));
    }

    private void saveSurveyQuestionsAndOptions(List<QuestionRequest> questions, Surveys surveys) {
        for (QuestionRequest question : questions) {
            SurveyQuestions surveyQuestions = surveyQuestionRepository.save(new SurveyQuestions(
                    generalService.generateSurveyQuestionId(),
                    surveys.getSurveyID(),
                    question.getQuestionText().trim()));
            for (QuestionOptionRequest optionRequest : question.getQuestionOptions()) {
                surveyQuestionOptionsRepository.save(new SurveyQuestionOptions(
                        generalService.generateQuestionOptionId(),
                        surveyQuestions.getQuestionID(),
                        optionRequest.getLabel().trim(),
                        optionRequest.getValue()));
            }
        }
    }

    private void _saveSurveyQuestionsAndOptions(List<QuestionRequest> questions, Surveys surveys) {
        List<SurveyQuestions> surveyQuestionsList = new ArrayList<>();
        List<SurveyQuestionOptions> surveyOptionsList = new ArrayList<>();

        for (QuestionRequest question : questions) {
            SurveyQuestions surveyQuestion = new SurveyQuestions(
                    generalService.generateSurveyQuestionId(),
                    surveys.getSurveyID(),
                    question.getQuestionText().trim()
            );
            surveyQuestionsList.add(surveyQuestion);
            for (QuestionOptionRequest optionRequest : question.getQuestionOptions()) {
                surveyOptionsList.add(new SurveyQuestionOptions(
                        generalService.generateQuestionOptionId(),
                        surveyQuestion.getQuestionID(),
                        optionRequest.getLabel().trim(),
                        optionRequest.getValue()
                ));
            }
        }

        // Bulk save for efficiency
        surveyQuestionRepository.saveAll(surveyQuestionsList);
        surveyQuestionOptionsRepository.saveAll(surveyOptionsList);
    }

    // Survey creation helper

    private StatusStudentResponse getStatusStudent(Surveys survey, SurveyResult surveyResult) {
        return surveyMapper.mapToResultStudent(
                surveyResult.getResult() + "/" + surveyResult.getMaxScore(),
                getStatusStudent(survey.getSurveyID(), surveyResult.getStudentID()),
                surveyResult.getStudentID(),
                surveyResult.getCompletionDate());
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

    private List<SurveysResponse> getChildrenSurveyResult(Set<Students> students, String ID) {
        List<Surveys> surveyList = surveyRepository.findAll();
        return surveyList.stream()
                .map(survey -> surveyMapper.buildSurveysResponse(survey,
                        getTotalQuestion(survey),
                        ID.contains("PAR") ? null : getCurrentPeriodStatusStudent(survey.getSurveyID(), ID),
                        mapStatusResponse(students, survey)))
                .collect(Collectors.toList());
    }


    private List<SurveysResponse> getSurveyResult(Students students, String ID) {
        List<Surveys> surveyList = surveyRepository.findAll();
        return surveyList.stream()
                .map(survey -> surveyMapper.buildSurveysResponse(survey,
                        getTotalQuestion(survey),
                        getCurrentPeriodStatusStudent(survey.getSurveyID(), ID),
                        mapStudentStatusResponse(students, survey)))
                .collect(Collectors.toList());
    }

    private List<StatusStudentResponse> mapStudentStatusResponse(Students student, Surveys survey) {
        List<SurveyResult> surveyResultSTD = surveyResultRepository
                .findBySurveyIDAndStudentID(survey.getSurveyID(), student.getStudentID());
        if (surveyResultSTD.isEmpty()) {
            return List.of();
        }
        if (surveyResultSTD.getLast().getCompletionDate().isBefore(survey.getStartDate())) {
            return List.of();
        }
        return surveyResultSTD.stream()
                .map(result -> surveyMapper.mapToResultStudent(
                        result.getResult() + "/" + result.getMaxScore(),
                        student.getStudentID(),
                        result.getCompletionDate())
                ).toList();

    }

    private List<StatusStudentResponse> mapStatusResponse(Set<Students> students, Surveys survey) {
        return students.stream()
                .flatMap(student -> {
                    List<SurveyResult> surveyResultSTD = surveyResultRepository
                            .findBySurveyIDAndStudentID(survey.getSurveyID(), student.getStudentID());
                    return surveyResultSTD.isEmpty()
                            ? Stream.of(surveyMapper.mapToResultStudent("0/0", student.getStudentID(), null))
                            : surveyResultSTD.stream()
                            .map(result -> {
                                        return surveyMapper.mapToResultStudent(
                                                result.getResult() + "/" + result.getMaxScore(),
                                                student.getStudentID(),
                                                result.getCompletionDate());
                                    }
                            );
                })
                .toList();
    }

    private int getTotalQuestion(Surveys surveys) {
        return (int) surveyQuestionRepository.countBySurveyID(surveys.getSurveyID());
    }

    private int getTotalStudentDone(Surveys surveys) {
        return surveyResultRepository.countDistinctStudentsBySurveyID(surveys.getSurveyID());
    }

    private int getTotalStudentDoneInCurrentPeriod(Surveys surveys) {
        LocalDateTime startDate = surveys.getStartDate();
        LocalDateTime endDate = surveys.getEndDate();
        return surveyResultRepository.countDistinctStudentsBySurveyIDAndCompletionDateBetween(
                surveys.getSurveyID(), startDate, endDate
        );
    }

    private int getTotalStudent() {
        return (int) studentRepository.count();
    }

    private String getSurveyStatus(Surveys surveys) {
        return getTotalStudent() == getTotalStudentDoneInCurrentPeriod(surveys) ? "COMPLETED" : "NOT COMPLETED";
    }

    private String getStatusStudent(String surveyId, String studentId) {
        return !surveyResultRepository.existsBySurveyIDAndStudentID(surveyId, studentId)
                ? "NOT COMPLETED"
                : "COMPLETED";
    }

    private String getCurrentPeriodStatusStudent(String surveyId, String studentId) {
        Surveys survey = surveyRepository.findBySurveyID(surveyId);
        return !surveyResultRepository.existsBySurveyIDAndStudentIDAndCompletionDateBetween(
                surveyId, studentId, survey.getStartDate(), survey.getEndDate())
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

    //// TEST FUNCTION

    public void periodicUpdateSurvey(String surveyID) {
        Surveys survey = surveyRepository.findBySurveyID(surveyID);
        if (survey == null) {
            throw new ResourceNotFoundException("Survey not found");
        }
        survey.setStartDate(LocalDateTime.now());
        survey.setEndDate(LocalDate.now().plusWeeks(survey.getDuration()).atStartOfDay());
        surveyRepository.save(survey);
    }

    public void periodicRestSurvey(String surveyID, String periodID) {
        Periodic periodic = periodicRepository.findByPeriodicID(periodID);
        Surveys survey = surveyRepository.findBySurveyID(surveyID);
        if (survey == null) {
            throw new ResourceNotFoundException("Survey not found");
        }
        survey.setStartDate(periodic.getStartDate());
        survey.setEndDate(periodic.getEndDate());
        survey.setDuration(periodic.getDuration());
        surveyRepository.save(survey);
    }

    public void repeatSurvey(String surveyID, String studentID) {
        Surveys survey = surveyRepository.findBySurveyID(surveyID);
        Students student = studentRepository.findByStudentID(studentID);
        if (survey == null) {
            throw new ResourceNotFoundException("Survey not found");
        }
        if (student == null) {
            throw new ResourceNotFoundException("Student not found");
        }
        SurveyResult surveyResult = surveyResultRepository.findBySurveyIDAndStudentID(surveyID, studentID).getLast();
        if (surveyResult == null) {
            throw new ResourceNotFoundException("Survey result not found");
        }
        surveyResult.setIsRepeat(true);
        surveyResultRepository.save(surveyResult);
    }
}