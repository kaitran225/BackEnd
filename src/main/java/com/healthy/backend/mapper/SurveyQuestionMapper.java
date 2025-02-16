package com.healthy.backend.mapper;

import java.util.List;

import org.springframework.stereotype.Component;

import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.SurveyResults;
import com.healthy.backend.entity.Surveys;

@Component
public class SurveyQuestionMapper {

    public SurveyQuestionResultResponse mapToSurveyQuestionResponse(SurveyResults surveyResults, SurveyQuestions surveyQuestions) {
        return SurveyQuestionResultResponse.builder()
                .questionId(surveyResults.getQuestionID())
                .categoryName(String.valueOf(surveyQuestions.getCategory().getCategoryName()))
                .questionText(surveyQuestions.getQuestionText())
                .resultId(surveyResults.getResultID())
                .answerId(surveyResults.getAnswerID())
                .score(surveyResults.getAnswer().getScore())               
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

    public SurveyResultsResponse mapToSurveyUpdate(SurveyQuestions surveyQuestions, List<SurveyQuestionResultResponse> questions) {
        return SurveyResultsResponse.builder()
                .questions(questions)
                .surveyId(surveyQuestions.getSurveyID())
                .build();
    }


}