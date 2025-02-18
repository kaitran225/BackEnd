package com.healthy.backend.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.healthy.backend.dto.survey.SurveyQuestionResult;
import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.entity.Answers;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.SurveyResults;
import com.healthy.backend.entity.Surveys;

@Component
public class SurveyQuestionMapper {

    public SurveyQuestionResultResponse mapToSurveyQuestionResponse(SurveyResults surveyResults, SurveyQuestions surveyQuestions, Answers ans) {
        return SurveyQuestionResultResponse.builder()
                .questionId(surveyResults.getQuestionID())
                .categoryName(String.valueOf(surveyQuestions.getCategory().getCategoryName()))
                .questionText(surveyQuestions.getQuestionText())
                .resultId(surveyResults.getResultID())
                .answerId(surveyResults.getAnswerID())
                .answer(ans.getAnswer())
                .score(surveyResults.getAnswer().getScore())               
                .build();                    
    }

    public SurveyQuestionResultResponse mapToSurveyQuestionResponse2(SurveyResults surveyResults, SurveyQuestions surveyQuestions, List<SurveyQuestionResultResponse> ans) {
        return SurveyQuestionResultResponse.builder()
                .questionId(surveyResults.getQuestionID())
                .categoryName(String.valueOf(surveyQuestions.getCategory().getCategoryName()))
                .questionText(surveyQuestions.getQuestionText())
                .answers(ans)
                .score(surveyResults.getAnswer().getScore())               
                .build();                    
    }

    public SurveyQuestionResult mapToListQuestion(List<SurveyQuestionResultResponse> surveyResults, List<SurveyQuestions> surveyQuestions) {
        return SurveyQuestionResult.builder()
                .surveyID(surveyQuestions.get(0).getSurveyID())
                .questions(surveyResults)
                .build();

    }

    public SurveyQuestionResultResponse mapToSurveyAns(SurveyResults surveyResults, Answers ans) {
        return SurveyQuestionResultResponse.builder()            
            .answerId(surveyResults.getAnswerID())
            .answer(ans.getAnswer())                             
            .build();                    
    }


    

    public SurveyQuestionResultResponse mapToSurveyQuestionResponse1(SurveyResults surveyResults, SurveyQuestions surveyQuestions, Answers ans) {
        return SurveyQuestionResultResponse.builder()
                .questionId(surveyResults.getQuestionID())
                .categoryName(String.valueOf(surveyQuestions.getCategory().getCategoryName()))
                .questionText(surveyQuestions.getQuestionText())                
                .answer(ans.getAnswer())               
                .build();                    
    }

     
    
    public SurveyResultsResponse mapToSurveyResultsResponse1(Surveys survey, List<SurveyQuestionResultResponse> questions, SurveyResults result) {
        return SurveyResultsResponse.builder()
                .surveyId(survey.getSurveyID())
                .surveyName(survey.getSurveyName())
                .description(survey.getDescription())            
                .studentId(result.getStudent().getStudentID())
                .questions(questions)
                .build();
    }

    // public SurveyQuestionResult mapToSurveyQuestion(List<SurveyQuestionResult> questions) {
    //     return SurveyQuestionResult.builder()
    //             .questions(questions)
    //             .build();   
    // }

    

}