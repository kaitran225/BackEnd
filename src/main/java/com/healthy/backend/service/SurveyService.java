package com.healthy.backend.service;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.healthy.backend.dto.survey.QuestionOption;
import com.healthy.backend.dto.survey.QuestionResponse;
import com.healthy.backend.dto.survey.SurveyQuestionResponse;
import com.healthy.backend.dto.survey.SurveysResponse;
import com.healthy.backend.entity.Answers;
import com.healthy.backend.entity.Categories;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.SurveyMapper;
import com.healthy.backend.repository.CategoriesRepository;
import com.healthy.backend.repository.SurveyAnswerRepository;
import com.healthy.backend.repository.SurveyQuestionRepository;
import com.healthy.backend.repository.SurveyRepository;
import com.healthy.backend.repository.SurveyResultRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SurveyService {
    private final SurveyResultRepository surveyResultRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyMapper surveyMapper;
    private final SurveyRepository surveyRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;
    private final CategoriesRepository categoriesRepository;

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
                    return surveyMapper.buildSurveysResponse(
                            survey, surveyQuestionsList.size());
                })
                .toList();
    }


    public void updateSurveyQuestion(String surveyID,SurveyQuestionResponse surveyQuestionResponse) {
        List<SurveyQuestions> surveyQuestions = surveyQuestionRepository.findBySurveyID(surveyID);
        
        if(surveyQuestions.isEmpty()) {
                throw new ResourceNotFoundException("surveyID not found"  + surveyID);
        }
        Map<String, SurveyQuestions> surveyQuestionMap = surveyQuestions.stream()
                .collect(Collectors.toMap(SurveyQuestions::getQuestionID , Function.identity()));

        for(QuestionResponse sqr : surveyQuestionResponse.getQuestionList()) {
                SurveyQuestions surveyQuestion1 = surveyQuestionMap.get(sqr.getId());
                Categories categories = categoriesRepository.findById(surveyQuestion1.getCategoryID())
                                .orElseThrow(() -> new ResourceNotFoundException("Categories not found" + surveyQuestion1.getCategoryID()));
                
                
                
                surveyQuestion1.setQuestionText(sqr.getQuestionText());
                categories.setCategoryName(Categories.MentalHealthCategory.valueOf(sqr.getQuestionCategory()));
                List<QuestionOption> questionOption = sqr.getQuestionOptions();

                List<Answers> answers = surveyAnswerRepository.findByQuestionID(surveyQuestion1.getQuestionID());

                if(!answers.isEmpty()) {
                        for(int i = 0; i < answers.size(); i++) {
                                Answers ans = answers.get(i);
                                        
                                if(i < questionOption.size()) {
                                        ans.setAnswer(questionOption.get(i).getLabel());
                                        ans.setScore(questionOption.get(i).getValue()); 

                                }                                                                 
                        }
                }
                surveyAnswerRepository.saveAll(answers);
                surveyQuestionRepository.save(surveyQuestion1); 
                categoriesRepository.save(categories);              
        }
}

//    public void updateSurveyQuestion(String questionID, String surveyID,
//                                     String answerID,
//                                     SurveyQuestionResult result1) {
//
//        SurveyQuestions surveyQuestions = surveyQuestionRepository.findByQuestionIDAndSurveyID(questionID, surveyID);
//        if (surveyQuestions == null) {
//            throw new ResourceNotFoundException("SurveyQuestion not found with questionID" + questionID + "and surveyID" + surveyID);
//        }
//
//        // surveyQuestions.setQuestionText(updateSurveyQuestion.getQuestionText());
//        surveyQuestions.setQuestionText(result1.getQuestionText());
//        surveyQuestionRepository.save(surveyQuestions);
//
//
//        Answers surveyResults = surveyAnswerRepository.findById(answerID)
//                .orElseThrow(() -> new ResourceNotFoundException("Answer not found" + answerID));
//
//
//        // Answers surveyAnswer = surveyResultRepository.findByAnswerID(result.getAnswerId());
//        // Answers surveyAnswer = surveyResultRepository.findByAnswerID(result1.getAnswerId());
//
//        // if (surveyAnswer == null) {
//        //     throw new ResourceNotFoundException("Answer not found with answerID " + result1.getAnswerId());
//        // }
//
//        surveyResults.setAnswer(result1.getAnswer());
//        surveyAnswerRepository.save(surveyResults);
//        questionAnswerMap.put(questionID, result1.getAnswerID());
//
//    }
//
//    public String getAnswerByQuestionText(String questionId) {
//        return questionAnswerMap.get(questionId);
//    }

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
                                    surveyAnswerRepository
                                            .findByQuestionID(questions.getQuestionID())
                                            .stream()
                                            .map(answer -> new QuestionOption(
                                                    answer.getScore(), answer.getAnswer()))
                                            .toList();
                            return surveyMapper.buildQuestionResponse
                                    (options, questions, id);
                        }
                )
                .toList();
        return surveyMapper.buildSurveyQuestionResponse(questionList, surveys);
    }
//    public SurveyQuestionResult getAllQuestionInSurveyID(String surveyID) {
//        List<SurveyQuestions> surveyQuestionsList = surveyQuestionRepository.findBySurveyID(surveyID);
//        List<SurveyResults> surveyResultses = new ArrayList<>();
//
//        if (surveyQuestionsList.isEmpty()) {
//            throw new ResourceNotFoundException("No questions found for surveyID " + surveyID);
//        }
//
//        Map<String, List<SurveyQuestionResultResponse>> answerMap = new HashMap<>();
//
//        for (SurveyQuestions surveyQuestions : surveyQuestionsList) {
//            List<SurveyResults> surveyResults = surveyResultRepository.findByStudentID(surveyQuestions.getQuestionID());
//
//            for (SurveyResults surveyResult : surveyResults) {
//                Answers answer = surveyAnswerRepository.findByAnswerID(surveyResult.getAnswerID());
//                SurveyQuestionResultResponse ans = surveyMapper.mapToSurveyAns(surveyResult, answer);
//
//                answerMap.computeIfAbsent(surveyResult.getQuestionID(), r -> new ArrayList<>()).add(ans);
//            }
//            surveyResultses.addAll(surveyResults);
//        }
//
//        List<SurveyQuestionResultResponse> surveyRes = new ArrayList<>();
//
//        answerMap.entrySet().stream()
//                .map(ansMap -> {
//                    String questionID = ansMap.getKey();
//                    List<SurveyQuestionResultResponse> sqr = ansMap.getValue();
//
//                    SurveyQuestions surveyQuestions = surveyQuestionRepository.findById(questionID)
//                            .orElseThrow(() -> new ResourceNotFoundException("QuestionID not found: " + questionID));
//
//                    SurveyResults surveyResult = surveyResultses.stream()
//                            .filter(s -> s.getQuestionID().equals(questionID))
//                            .findFirst()
//                            .orElseThrow(() -> new ResourceNotFoundException("SurveyResults not found"));
//
//
//                    SurveyQuestionResultResponse surveyQuestionResultResponse = surveyMapper.mapToSurveyQuestionResponse2(surveyResult, surveyQuestions, sqr);
//                    surveyRes.add(surveyQuestionResultResponse);
//
//                    return surveyMapper.mapToListQuestion(surveyRes, surveyQuestionsList);
//                })
//                .collect(Collectors.toList());
//
//        return surveyMapper.mapToListQuestion(surveyRes, surveyQuestionsList);
//    }
}


