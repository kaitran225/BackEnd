package com.healthy.backend.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.healthy.backend.dto.survey.SurveyQuestionResult;
import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.entity.Answers;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.SurveyResults;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.exception.ResourceNotFoundException;
import com.healthy.backend.mapper.SurveyQuestionMapper;
import com.healthy.backend.mapper.SurveyResultMapper;
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
    private final SurveyResultMapper surveyMapper;
    private final SurveyQuestionMapper surveyQuestionMapper;
    private final SurveyRepository surveyRepository;
    private final SurveyAnswerRepository surveyAnswerRepository;
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

        Map<String, List<SurveyQuestionResultResponse>> surveyResultsResponseMap = new HashMap<>();
        surveyResults
                    .forEach(results -> {
                        SurveyQuestions surveyQuestions = surveyQuestionMap.get(results.getQuestionID());
                        Answers ans = surveyAnswerRepository.findByAnswerID(results.getAnswerID());
                        SurveyQuestionResultResponse surveyQuestionResultResponse = surveyQuestionMapper.mapToSurveyQuestionResponse(results, surveyQuestions, ans);
                        Surveys survey = surveyRepository.findById(surveyQuestions.getSurveyID())
                                                        .orElseThrow(() -> new ResourceNotFoundException("No found Survey"));
                        surveyResultsResponseMap.computeIfAbsent(survey.getSurveyID(), k -> new ArrayList<>()).add(surveyQuestionResultResponse);

                    });

        return surveyResultsResponseMap.entrySet()            
                                        .stream()
                                        .map(entry -> {
                                            String surveyId = entry.getKey();
                                            List<SurveyQuestionResultResponse>  surveyRes = entry.getValue();

                                            Surveys survey = surveyRepository.findById(surveyId)
                                                        .orElseThrow(() -> new ResourceNotFoundException("No found Survey"));

                                            SurveyResults result = surveyResults.stream()
                                                        .filter(r -> surveyQuestionMap.get(r.getQuestionID()).getSurveyID()
                                                        .equals(surveyId))            
                                                        .findFirst()
                                                        .orElseThrow(() -> new ResourceNotFoundException("SurveyResults not found"));

                                            return surveyQuestionMapper.mapToSurveyResultsResponse1(survey, surveyRes, result);
                                        })
                                        .collect(Collectors.toList());
   
    }

    public void updateSurveyQuestion(String questionID, String surveyID, 
                                    String answerID,             
                                    SurveyQuestionResult result1) {
        
        SurveyQuestions surveyQuestions = surveyQuestionRepository.findByQuestionIDAndSurveyID(questionID, surveyID);
            if(surveyQuestions == null) {
                throw new ResourceNotFoundException("SurveyQuestion not found with questionID" + questionID + "and surveyID" + surveyID);
            }
        
        // surveyQuestions.setQuestionText(updateSurveyQuestion.getQuestionText());
        surveyQuestions.setQuestionText(result1.getQuestionText());
        surveyQuestionRepository.save(surveyQuestions);


        Answers surveyResults = surveyAnswerRepository.findById(answerID)
                                .orElseThrow(() -> new ResourceNotFoundException("Answer not found" + answerID));
                                                         
        surveyResults.setAnswer(result1.getAnswer());
        surveyAnswerRepository.save(surveyResults);
        questionAnswerMap.put(questionID, result1.getAnswerID());

    }
    
    public String getAnswerByQuestionText(String questionId) {
        return questionAnswerMap.get(questionId);
    }

    public SurveyQuestionResult getAllQuestionInSurveyID(String surveyID) {
    List<SurveyQuestions> surveyQuestionsList = surveyQuestionRepository.findBySurveyID(surveyID);
    List<SurveyResults> surveyResultses = new ArrayList<>(); 

    if(surveyQuestionsList.isEmpty()) {
        throw new ResourceNotFoundException("No questions found for surveyID " + surveyID);
    }

    Map<String, List<SurveyQuestionResultResponse>> answerMap = new HashMap<>();

    for (SurveyQuestions surveyQuestions : surveyQuestionsList) {
        List<SurveyResults> surveyResults = surveyResultRepository.findByQuestionID(surveyQuestions.getQuestionID());

        for (SurveyResults surveyResult : surveyResults) {
            Answers answer = surveyAnswerRepository.findByAnswerID(surveyResult.getAnswerID());
            SurveyQuestionResultResponse ans = surveyQuestionMapper.mapToSurveyAns(surveyResult, answer);

            answerMap.computeIfAbsent(surveyResult.getQuestionID(), r -> new ArrayList<>()).add(ans);
        }
        surveyResultses.addAll(surveyResults);
    }

    List<SurveyQuestionResultResponse> surveyRes = new ArrayList<>();
    
    answerMap.entrySet().stream()
        .map(ansMap -> {
            String questionID = ansMap.getKey();
            List<SurveyQuestionResultResponse> sqr = ansMap.getValue();

            SurveyQuestions surveyQuestions = surveyQuestionRepository.findById(questionID)
                .orElseThrow(() -> new ResourceNotFoundException("QuestionID not found: " + questionID));

            SurveyResults surveyResult = surveyResultses.stream()
                .filter(s -> s.getQuestionID().equals(questionID))
                .findFirst()
                .orElseThrow(() -> new ResourceNotFoundException("SurveyResults not found"));

            
            SurveyQuestionResultResponse surveyQuestionResultResponse = surveyQuestionMapper.mapToSurveyQuestionResponse2(surveyResult, surveyQuestions, sqr);
            surveyRes.add(surveyQuestionResultResponse);

            return surveyQuestionMapper.mapToListQuestion(surveyRes, surveyQuestionsList);
        })
        .collect(Collectors.toList());

    return surveyQuestionMapper.mapToListQuestion(surveyRes, surveyQuestionsList);
}

}


