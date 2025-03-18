package com.healthy.backend.mapper;

import com.healthy.backend.dto.survey.QuestionOption;
import com.healthy.backend.dto.survey.response.*;
import com.healthy.backend.entity.SurveyQuestionOptions;
import com.healthy.backend.entity.SurveyQuestionOptionsChoices;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.Surveys;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.List;

@Component
public class SurveyMapper {


    public SurveysResponse buildManagerSurveysResponse(Surveys survey, int numberOfQuestions, String completeStatus, String count) {
        return SurveysResponse.builder()
                .id(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .category(survey.getCategory().name())
                .standardType(survey.getStandardType().name())
                .periodic(survey.getPeriodic())
                .numberOfQuestions(numberOfQuestions)
                .status(String.valueOf(survey.getStatus()))
                .createdAt(survey.getCreatedAt().toString())
                .createBy(survey.getCreator().getFullName())
                .completeStatus(completeStatus)
                .studentComplete(count)
                .build();
    }

    public SurveysResponse buildSurveysResponse(Surveys survey, int numberOfQuestions, String completeStatus, String score) {
        return SurveysResponse.builder()
                .id(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .numberOfQuestions(numberOfQuestions)
                .category(survey.getCategory().name())
                .standardType(survey.getStandardType().name())
                .periodic(survey.getPeriodic())
                .status(String.valueOf(survey.getStatus()))
                .createdAt(survey.getCreatedAt().toString())
                .createBy(survey.getCreator().getFullName())
                .completeStatus(completeStatus)
                .score(score)
                .build();
    }

    public SurveysResponse buildSurveysResponse(Surveys survey, int numberOfQuestions, String completeStatus, List<StatusStudentResponse> status) {
        return SurveysResponse.builder()
                .id(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .numberOfQuestions(numberOfQuestions)
                .status(String.valueOf(survey.getStatus()))
                .createdAt(survey.getCreatedAt().toString())
                .category(survey.getCategory().name())
                .standardType(survey.getStandardType().name())
                .periodic(survey.getPeriodic())
                .createBy(survey.getCreator().getFullName())
                .completeStatus(completeStatus)
                .statusStudentResponse(status)
                .build();
    }

    public StatusStudentResponse mapToResultStudent(String score, String status, String studentId, LocalDate
            date) {
        return StatusStudentResponse.builder()
                .studentComplete(status)
                .lastCompleteDate(date)
                .score(score)
                .studentId(studentId)
                .build();
    }

    public StatusStudentResponse mapToResultStudent(String score, String studentId, LocalDate date) {
        return StatusStudentResponse.builder()
                .score(score)
                .studentId(studentId)
                .lastCompleteDate(date)
                .build();
    }

    public SurveyResultsResponse mapToListResultsResponse(Surveys survey, List<StatusStudentResponse> std) {
        return SurveyResultsResponse.builder()
                .surveyId(survey.getSurveyID())
                .surveyName(survey.getSurveyName())
                .description(survey.getDescription())
                .category(survey.getCategory().name())
                .standardType(survey.getStandardType().name())
                .periodic(survey.getPeriodic())
                .status((survey.getStatus().name()))
                .std(std)
                .build();
    }


    public SurveyQuestionResultResponse mapToSurveyQuestionResultResponse(SurveyQuestionOptionsChoices result) {
        return SurveyQuestionResultResponse.builder()
                .questionId(result.getQuestionID())
                .questionText(result.getQuestion().getQuestionText())
                .resultId(result.getOptionsChoicesID())
                .answerId(result.getOptions().getOptionID())
                .answer(result.getOptions().getOptionText())
                .score(result.getOptions().getScore())
                .build();
    }

    public SurveyResultsResponse mapToSurveyResultsResponse(Surveys survey, List<SurveyQuestionResultResponse> questions) {
        return SurveyResultsResponse.builder()
                .surveyId(survey.getSurveyID())
                .surveyName(survey.getSurveyName())
                .description(survey.getDescription())
                .category(survey.getCategory().name())
                .standardType(survey.getStandardType().name())
                .periodic(survey.getPeriodic())
                .status((survey.getStatus().name()))
                .questions(questions)
                .build();
    }


    public SurveyQuestionResponse buildSurveyQuestionResponse(
            List<QuestionResponse> questionResponseList,
            Surveys survey
    ) {
        return SurveyQuestionResponse.builder()
                .surveyId(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .category(survey.getCategory().name())
                .standardType(survey.getStandardType().name())
                .periodic(survey.getPeriodic())
                .questionList(questionResponseList)
                .build();
    }

    public SurveyQuestionResponse buildSurveyResultResponse(
            List<QuestionResponse> questionResponseList,
            Surveys survey, String completeStatus, String score
    ) {
        return SurveyQuestionResponse.builder()
                .surveyId(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .category(survey.getCategory().name())
                .standardType(survey.getStandardType().name())
                .periodic(survey.getPeriodic())
                .numberOfQuestions(questionResponseList.size())
                .questionList(questionResponseList)
                .completeStatus(completeStatus)
                .totalScore(score)
                .build();
    }

    public QuestionResponse buildQuestionResponse(
            List<QuestionOption> questionOption,
            SurveyQuestions surveyQuestions,
            Integer index) {
        return QuestionResponse.builder()
                .id(index.toString())
                .questionText(surveyQuestions.getQuestionText())
                .questionOptions(questionOption)
                .build();
    }

    public QuestionOption buildNewQuestionOption(SurveyQuestionOptions options) {
        return QuestionOption.builder()
                .answerID(options.getOptionID())
                .label(options.getOptionText())
                .value(options.getScore())
                .checked(false)
                .build();
    }

    public SurveysResponse buildSurveysResponse(Surveys survey) {
        return SurveysResponse.builder()
                .id(survey.getSurveyID())
                .title(survey.getSurveyName())
                .description(survey.getDescription())
                .category(survey.getCategory().name())
                .standardType(survey.getStandardType().name())
                .periodic(survey.getPeriodic())
                .status(String.valueOf(survey.getStatus()))
                .createBy(survey.getCreator().getUsername())
                .build();
    }
}