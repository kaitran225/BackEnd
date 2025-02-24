package com.healthy.backend.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.healthy.backend.dto.survey.QuestionOption;
import com.healthy.backend.dto.survey.QuestionResponse;
import com.healthy.backend.dto.survey.StatusStudent;
import com.healthy.backend.dto.survey.SurveyQuestionResponse;
import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.survey.SurveysResponse;
import com.healthy.backend.entity.Categories;
import com.healthy.backend.entity.SurveyQuestionOptions;
import com.healthy.backend.entity.SurveyQuestionOptionsChoices;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.SurveyResult;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.enums.SurveyCategory;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.SurveyMapper;
import com.healthy.backend.repository.CategoriesRepository;
import com.healthy.backend.repository.SurveyQuestionOptionsChoicesRepository;
import com.healthy.backend.repository.SurveyQuestionOptionsRepository;
import com.healthy.backend.repository.SurveyQuestionRepository;
import com.healthy.backend.repository.SurveyRepository;
import com.healthy.backend.repository.SurveyResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyQuestionOptionsChoicesRepository surveyQuestionOptionsChoicesRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyMapper surveyMapper;
    private final SurveyRepository surveyRepository;
    private final SurveyQuestionOptionsRepository surveyQuestionOptionsRepository;
    private final CategoriesRepository categoriesRepository;
    private final SurveyResultRepository surveyResultRepository;


    public String getStatusStudent(List<SurveyResult> surveyResultList) {
        List<String> resultList = new ArrayList<>();
        for(SurveyResult surveyStudent : surveyResultList) {
                SurveyResult surveyResult = surveyResultRepository.findByResultID(surveyStudent.getResultID());
                String result = (surveyResult != null) ? "Finished" : "Not Finished";
                resultList.add(result);
        }

        for (String test : resultList) {
                if("Not Finished".equals(test)) {
                        return "Not Finished";
                }
        }
        return "Finished";
    }

    public SurveysResponse getStudentIDSurveyResults(String surveyId, String studentId) {
        Surveys survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("Survey not found by"+ surveyId));

        List<SurveyResult> surveyResultList = surveyResultRepository.findBySurveyID(surveyId);
        if(surveyResultList.isEmpty()) {
                throw new ResourceNotFoundException("Not found survey " + surveyId);
        }
        List<SurveyQuestions> surveyQuestionList = surveyQuestionRepository.findBySurveyID(surveyId);
        if(surveyQuestionList.isEmpty()) {
                throw new ResourceNotFoundException("Not found question in this survey");
        }
        
        
        List<StatusStudent> statusList = new ArrayList<>();
        boolean found = false;

        for(SurveyResult result : surveyResultList) {
                if(result.getStudentID().equals(studentId)) {
                        List<SurveyResult> surveyStudentList = surveyResultRepository.findByStudentID(result.getStudentID());
                        if(!surveyStudentList.isEmpty()) {
                                StatusStudent status = surveyMapper.buildStatusStudent(result, getStatusStudent(surveyStudentList));
                                statusList.add(status);
                                found = true;
                                break;
                        }
                        else {
                                throw new ResourceNotFoundException("Not found student in this survey");
                        }
                        
                }        

        }
        if (!found) {
                throw new ResourceNotFoundException("Student not found in this survey");
        }
        return surveyMapper.buildSurveysResponse1(survey, surveyQuestionList.size(), statusList );      
    }

    public List<SurveysResponse> getAllSurveys() {

        List<Surveys> surveys = surveyRepository.findAll();

        if (surveys.isEmpty()) {
            throw new ResourceNotFoundException("No surveys found");
        }
        return surveys
                .stream()
                .map(survey
                        -> {
                    List<SurveyQuestions> surveyQuestionsList
                        = surveyQuestionRepository.findBySurveyID(survey.getSurveyID());

                    List<SurveyResult> surveyResultList = surveyResultRepository.findBySurveyID(survey.getSurveyID());
                    List<StatusStudent> statusStudentList = surveyResultList.stream()
                                .map(surveyResult -> {
                                        List<SurveyResult> surveyStudentList = surveyResultRepository.findByStudentID(surveyResult.getStudentID());
                                        StatusStudent statusStudent = surveyMapper.buildStatusStudent(surveyResult, getStatusStudent(surveyStudentList));                                
                                        return statusStudent;
                                })
                                .collect(Collectors.toList());    
                    return surveyMapper.buildSurveysResponse1(
                            survey, surveyQuestionsList.size(), statusStudentList);
                })
                .toList();
    }

    public void updateSurveyQuestion(String surveyID, SurveyQuestionResponse surveyQuestionResponse) {
        List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);

        if (surveyQuestions.isEmpty()) {
            throw new ResourceNotFoundException("surveyID not found" + surveyID);
        }
        Map<String, SurveyQuestions> surveyQuestionMap = surveyQuestions.stream()
                .collect(Collectors.toMap(SurveyQuestions::getQuestionID, Function.identity()));

        for (QuestionResponse sqr : surveyQuestionResponse.getQuestionList()) {
            SurveyQuestions surveyQuestion1 = surveyQuestionMap.get(sqr.getId());

            Categories categories = categoriesRepository.findById(surveyQuestion1.getCategoryID())
                    .orElseThrow(() -> new ResourceNotFoundException("Categories not found" + surveyQuestion1.getCategoryID()));


            surveyQuestion1.setQuestionText(sqr.getQuestionText());
            categories.setCategoryName(SurveyCategory.valueOf(sqr.getQuestionCategory()));
            List<QuestionOption> questionOption = sqr.getQuestionOptions();

            List<SurveyQuestionOptions> answers = surveyQuestionOptionsRepository.findByQuestionID(surveyQuestion1.getQuestionID());

            if (!answers.isEmpty()) {
                for (int i = 0; i < answers.size(); i++) {
                    SurveyQuestionOptions ans = answers.get(i);

                    if (i < questionOption.size()) {
                        ans.setOptionText(questionOption.get(i).getLabel());
                        ans.setScore(questionOption.get(i).getValue());

                    }
                }
            }
            surveyQuestionOptionsRepository.saveAll(answers);
            surveyQuestionRepository.save(surveyQuestion1);
            categoriesRepository.save(categories);
        }
    }

    public SurveyQuestionResponse getSurveyQuestion(String surveyID) {

        Surveys surveys = surveyRepository.findById(surveyID).orElseThrow(
                () -> new ResourceNotFoundException("No survey found for surveyID " + surveyID)
        );
        List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);
        if (surveyQuestions.isEmpty()) {
            throw new ResourceNotFoundException("No questions found for surveyID " + surveyID);
        }

        List<QuestionResponse> questionList = surveyQuestions
                .stream()
                .map(questions -> {
                            Integer id = surveyQuestions.indexOf(questions);
                            List<QuestionOption> options =
                                    surveyQuestionOptionsRepository
                                            .findByQuestionID(questions.getQuestionID())
                                            .stream()
                                            .map(answer -> new QuestionOption(
                                                    answer.getScore(), answer.getOptionText(), answer.getOptionID()))
                                            .toList();
                            return surveyMapper.buildQuestionResponse
                                    (options, questions, id);
                        }
                )
                .toList();
        return surveyMapper.buildSurveyQuestionResponse(questionList, surveys);
    }


    public String getLastIdInDB(String index) {
        String prefix = index.replaceAll("\\d", "");
        String numberPart = index.replaceAll("\\D", "");

        if (numberPart.isEmpty()) {
            throw new IllegalArgumentException("No numeric found in index: " + index);
        }

        int currentID = Integer.parseInt(numberPart);
        try {
            currentID = Integer.parseInt(numberPart);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid numeric part in index: " + numberPart);
        }
        int newID = currentID + 1;
        String formatID = prefix + String.format("%0" + numberPart.length() + "d", newID);

        return formatID;
    }


    public void addSurveyQuestion(String surveyId, SurveyQuestionResponse questionResponse) {
        List<SurveyQuestions> surveyQuestion = surveyQuestionRepository.findBySurveyID(surveyId);
        if (surveyQuestion.isEmpty()) {
            throw new ResourceNotFoundException("Not found survey for " + surveyId);
        }

        for (QuestionResponse questionRes : questionResponse.getQuestionList()) {

            SurveyQuestions newQuestion = surveyQuestionRepository.findFirstByOrderByQuestionIDDesc();
            System.out.println("newQuestion.." + newQuestion.getQuestionID());
            String latestQuestion = (newQuestion != null) ? getLastIdInDB(newQuestion.getQuestionID()) : "Q0001";

            System.out.println("newQuestion1.." + latestQuestion);

            SurveyQuestions surveyQuestion1 = new SurveyQuestions();
            surveyQuestion1.setQuestionText(questionRes.getQuestionText());
            surveyQuestion1.setQuestionID(latestQuestion);
            surveyQuestion1.setSurveyID(surveyId);

            SurveyCategory categories;
            try {
                categories = SurveyCategory.valueOf(questionRes.getQuestionCategory());
            } catch (IllegalArgumentException ex) {
                throw new ResourceNotFoundException("Category not found " + questionRes.getQuestionCategory());
            }

            Categories category = categoriesRepository.findByCategoryName(categories);

            surveyQuestion1.setCategoryID(category.getCategoryID());
            surveyQuestionRepository.save(surveyQuestion1);

            List<SurveyQuestionOptions> optionsList = new ArrayList<>();
            List<QuestionOption> questionOption = questionRes.getQuestionOptions();

            String nextAnsId = null;
            int index = 0;
            SurveyQuestionOptions lastAns = null;
            for (QuestionOption QO : questionOption) {
                SurveyQuestionOptions option = new SurveyQuestionOptions();
                option.setOptionText(QO.getLabel());
                option.setScore(QO.getValue());

                if (index == 0) {
                    lastAns = surveyQuestionOptionsRepository.findFirstByOrderByOptionIDDesc();
                    nextAnsId = lastAns.getOptionID();
                }

                String newAns = getLastIdInDB(nextAnsId);
                nextAnsId = newAns;
                option.setQuestionID(newAns);
                option.setQuestionID(latestQuestion);
                optionsList.add(option);
                index++;
            }
            surveyQuestionOptionsRepository.saveAll(optionsList);

        }

    }

    public SurveyResultsResponse getSurveyResults(String surveyId) {
        Surveys survey = surveyRepository.findById(surveyId)
                .orElseThrow(() -> new ResourceNotFoundException("SurveyId not found" + surveyId));
        List<SurveyQuestions> surveyQuestion = surveyQuestionRepository.findBySurveyID(surveyId);

        List<SurveyQuestionResultResponse> questionResultList = surveyQuestion.stream()
                .map(surveyQuestion1 -> {
                    List<SurveyQuestionOptions> optionsList = surveyQuestionOptionsRepository.findByQuestionID(surveyQuestion1.getQuestionID());
                    List<QuestionOption> questionOption1 = new ArrayList<>();

                    optionsList
                            .forEach(options -> {
                                QuestionOption questionOption = surveyMapper.mapToQuestionOption(options);
                                questionOption1.add(questionOption);
                            });
                    SurveyQuestionOptionsChoices surveyResults = surveyQuestionOptionsChoicesRepository.findByQuestionID(surveyQuestion1.getQuestionID())
                            .stream()
                            .findFirst()
                            .orElse(null);
                    List<SurveyQuestionResultResponse> questionResults = new ArrayList<>();
                    if (surveyResults != null) {
                        SurveyQuestionResultResponse sqr = surveyMapper.mapToSurveyQuestionResponse(surveyResults, surveyQuestion1, questionOption1);
                        questionResults.add(sqr);
                    }
                    return questionResults;

                })
                .flatMap(List::stream)
                .collect(Collectors.toList());
        return surveyMapper.mapToSurveyResultsResponse1(survey, questionResultList);
    }

    public void addAnswerToQuestion(String surveyId, String questionId, List<QuestionOption> answerOption) {
        List<SurveyQuestions> surveyQuestionList = surveyQuestionRepository.findBySurveyID(surveyId);
        Map<String, SurveyQuestions> surveyQuestionMap = surveyQuestionList.stream()
                .collect(Collectors.toMap(SurveyQuestions::getQuestionID, Function.identity()));

        SurveyQuestions surveyQuestion = surveyQuestionMap.get(questionId);
        List<SurveyQuestionOptions> optionsList = new ArrayList<>();
        int index = 0;
        String aswersDesc1 = null;
        SurveyQuestionOptions answerDesc = null;
        for (QuestionOption questionOption : answerOption) {
            SurveyQuestionOptions options = new SurveyQuestionOptions();

            if (index == 0) {
                answerDesc = surveyQuestionOptionsRepository.findFirstByOrderByOptionIDDesc();
                aswersDesc1 = answerDesc.getOptionID();
            }

            String numberOfAns = getLastIdInDB(aswersDesc1);
            aswersDesc1 = numberOfAns;

            options.setQuestionID(numberOfAns);
            options.setOptionText(questionOption.getLabel());
            options.setScore(questionOption.getValue());
            options.setQuestionID(surveyQuestion.getQuestionID());
            optionsList.add(options);
            index++;

        }
        surveyQuestionOptionsRepository.saveAll(optionsList);
    }

    
}