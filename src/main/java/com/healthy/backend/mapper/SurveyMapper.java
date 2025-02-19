package com.healthy.backend.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.healthy.backend.dto.survey.*;
import com.healthy.backend.exception.ResourceNotFoundException;
import org.springframework.stereotype.Component;

import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.SurveyResults;
import com.healthy.backend.entity.Surveys;

@Component
public class SurveyMapper {

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

    public List<SurveyResultsResponse> getUserSurveyResults(List<SurveyResults> surveyResults) {
        if (surveyResults == null) throw new ResourceNotFoundException("No survey results found");
        return buildSurveyResults(surveyResults);
    }

    public  List<SurveyResultsResponse> buildSurveyResults(List<SurveyResults> surveyResults) {

        Map<String, List<SurveyQuestionResultResponse>> groupedResults =
                surveyResults
                        .stream()
                        .collect(Collectors.groupingBy(
                                result ->
                                        result.getQuestion().getSurveyID(),
                                Collectors.mapping(this::mapToSurveyQuestionResultResponse, Collectors.toList())
                        ));

        Map<String, Surveys> surveyMap = groupedResults.keySet().stream()
                .collect(Collectors.toMap(key -> key, key -> new Surveys()));
        return groupedResults.entrySet().stream()
                .map(entry -> mapToSurveyResultsResponse(surveyMap.get(entry.getKey()), entry.getValue()))
                .toList();
    }


    public SurveyQuestionResultResponse mapToSurveyQuestionResultResponse(SurveyResults result) {
        return SurveyQuestionResultResponse.builder()
                .questionId(result.getQuestionID())
                .categoryName(String.valueOf(result.getQuestion().getCategory().getCategoryName())) // No LazyInitializationException
                .questionText(result.getQuestion().getQuestionText())
                .resultId(result.getResultID())
                .answerId(result.getAnswer().getAnswerID())
                .answer(result.getAnswer().getAnswer())
                .score(result.getAnswer().getScore())
                .build();
    }

    public SurveyResultsResponse mapToSurveyResultsResponse(Surveys survey, List<SurveyQuestionResultResponse> questions) {
        return SurveyResultsResponse.builder()
                .surveyId(survey.getSurveyID())
                .surveyName(survey.getSurveyName())
                .description(survey.getDescription())
                .questions(questions)
                .build();
    }

    public SurveysResponse buildSurveysResponse(Surveys survey, int numberOfQuestions) {
        return SurveysResponse.builder()
                .id(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .categoryID(survey.getCategory().getCategoryID())
                .duration(survey.getDuration())
                .numberOfQuestions(numberOfQuestions)
                .categoryName(survey.getCategory().getCategoryName().name())
                .status(String.valueOf(survey.getStatus()))
                .detailedDescription(survey.getDetails())
                .createdAt(survey.getCreatedAt().toString())
                .createBy(survey.getCreator().getFullName())
                .build();
    }

    public SurveyQuestionResponse buildSurveyQuestionResponse(
            List<QuestionResponse> questionResponseList,
            Surveys survey
    ) {
        return SurveyQuestionResponse.builder()
                .surveyId(survey.getSurveyID())
                .title(survey.getSurveyName())
                .questionList(questionResponseList)
                .build();
    }

    public QuestionResponse buildQuestionResponse(
            List<QuestionOption> questionOption,
            SurveyQuestions surveyQuestions,
            Integer index) {
        return QuestionResponse.builder()
                .id(index.toString())
                .questionText(surveyQuestions.getQuestionText())
                .questionCategory(surveyQuestions.getCategory().getCategoryName().name())
                .questionOptions(questionOption)
                .build();
    }

    public SurveysResponse buildSurveysResponse(Surveys survey) {
        return SurveysResponse.builder()
                .id(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .categoryID(survey.getCategory().getCategoryID())
                .duration(survey.getDuration())
                .categoryName(survey.getCategory().getCategoryName().name())
                .status(String.valueOf(survey.getStatus()))
                .detailedDescription(survey.getDetails())
                .createBy(survey.getCreator().getFullName())
                .build();
    }
}