package com.healthy.backend.service;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.healthy.backend.dto.survey.*;
import com.healthy.backend.mapper.SurveyMapper;
import org.springframework.stereotype.Service;

import com.healthy.backend.entity.Answers;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.SurveyResults;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.exception.ResourceNotFoundException;
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


