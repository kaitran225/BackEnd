package com.healthy.backend.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.healthy.backend.dto.survey.SurveyQuestionResponse;
import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.entity.Answers;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.SurveyResults;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.SurveyQuestionMapper;
import com.healthy.backend.mapper.SurveyResultMapper;
import com.healthy.backend.repository.SurveyQuestionRepository;
import com.healthy.backend.repository.SurveyRepository;
import com.healthy.backend.repository.SurveyResultRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class SurveyService {
    private final SurveyResultRepository surveyResultRepository;
    private final SurveyQuestionRepository surveyQuestionRepository;
    private final SurveyResultMapper surveyMapper;
    private final SurveyQuestionMapper surveyQuestionMapper;
    private final SurveyRepository surveyRepository;
    private Map<String, String> questionAnswerMap = new HashMap<>();
    

    public List<SurveyResultsResponse> getAllSurveyResults() {
        List<SurveyResults> surveyResults = surveyResultRepository.findAll();

        List<String> questionID = surveyResults.stream()
                                  .map(SurveyResults :: getQuestionID)
                                  .distinct()
                                  .collect(Collectors.toList());

        List<SurveyQuestions> getQuestionID = surveyQuestionRepository.findByQuestionIDIn(questionID);

        Map<String, SurveyQuestions> surveyQuestionMap = getQuestionID.stream()
                                                        .collect(Collectors.toMap(SurveyQuestions :: getQuestionID, Function.identity()));

        return surveyResults.stream()
               .map(result -> {
                    SurveyQuestions surveyQuestion = surveyQuestionMap.get(result.getQuestionID());
                    Surveys surveys = surveyRepository.findById(surveyQuestion.getSurveyID())
                        .orElseThrow(() -> new ResourceNotFoundException(" Survey not found"));
                    SurveyQuestionResultResponse surveyResultsResponse = surveyQuestionMapper.mapToSurveyQuestionResponse
                                                                    (result, surveyQuestion);
                    // return surveyMapper.mapToSurveyResultsResponse(surveys, Collections.singletonList(surveyResultsResponse))  ;     
                    return surveyQuestionMapper.mapToSurveyResultsResponse1(surveys, Collections.singletonList(surveyResultsResponse), result);                                            
               })
               .collect(Collectors.toList());
                                                         

    }

    public void updateSurveyQuestion(String questionID, String surveyID, SurveyQuestionResponse updateSurveyQuestion, SurveyQuestionResultResponse result) {
        
        SurveyQuestions surveyQuestions = surveyQuestionRepository.findByQuestionIDAndSurveyID(questionID, surveyID);
            if(surveyQuestions == null) {
                throw new ResourceNotFoundException("SurveyQuestion not found with questionID" + questionID + "and surveyID" + surveyID);
            }
        
        surveyQuestions.setQuestionText(updateSurveyQuestion.getQuestionText());
        surveyQuestionRepository.save(surveyQuestions);


        SurveyResults surveyResults = surveyResultRepository.findByQuestionID(questionID);
            if(surveyResults == null) {
                throw new ResourceNotFoundException("QuestionID not found with questionID" + questionID);
            }                                                
        
        Answers surveyAnswer = surveyResultRepository.findByAnswerID(result.getAnswerId());
         if (surveyAnswer == null) {
            throw new ResourceNotFoundException("Answer not found with answerID " + result.getAnswerId());
        }   

        surveyResults.setAnswer(surveyAnswer);
        surveyResultRepository.save(surveyResults);
        questionAnswerMap.put(questionID, result.getAnswer());

    }
    
    public String getAnswerByQuestionText(String questionId) {
        return questionAnswerMap.get(questionId);
    }

}


