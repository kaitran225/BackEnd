package com.healthy.backend.mapper;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

import com.healthy.backend.dto.survey.QuestionOption;
import com.healthy.backend.dto.survey.QuestionResponse;
import com.healthy.backend.dto.survey.StatusStudent;
import com.healthy.backend.dto.survey.SurveyQuestionResponse;
import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.dto.survey.SurveysResponse;
import com.healthy.backend.entity.SurveyQuestionOptions;
import com.healthy.backend.entity.SurveyQuestionOptionsChoices;
import com.healthy.backend.entity.SurveyQuestions;
import com.healthy.backend.entity.SurveyResult;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.exception.ResourceNotFoundException;

@Component
public class SurveyMapper {


    public SurveysResponse buildSurveysResponse1(Surveys survey, int numberOfQuestions, List<StatusStudent> statusStudent, List<SurveyQuestionResultResponse> questions) {
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
                .createBy(survey.getCreator().getUsername())
                .statusStudent(statusStudent)
                .questions(questions)
                .build();
        }

      public SurveysResponse buildSurveysResponse2(Surveys survey, int numberOfQuestions, List<StatusStudent> statusStudent) {
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
                .createBy(survey.getCreator().getUsername())
                .statusStudent(statusStudent)
                .build();
        } 

     public StatusStudent buildStatusStudent(SurveyResult surveyResult, String getStatusStudent, int score) {
         return StatusStudent.builder()
                .status(getStatusStudent)
                .studentId(surveyResult.getStudentID())
                .score(score)
                .build();
        }


    public SurveyQuestionResultResponse mapToQuestion(SurveyQuestions surveyQuestions,List<QuestionOption> answers) {
        return SurveyQuestionResultResponse.builder()
                .questionId(surveyQuestions.getQuestionID())
                .categoryName(String.valueOf(surveyQuestions.getCategory().getCategoryName()))
                .questionText(surveyQuestions.getQuestionText())
                .answers(answers)
                .build();
        }    

     public QuestionOption mapToQuestionOption(SurveyQuestionOptions options) {
        return QuestionOption.builder()
                .label(options.getOptionText())
                .value(options.getScore())
                .build();
        }

      public StatusStudent maptoResultStudent1(String status, int score, SurveyResult surveyResult ){
        return StatusStudent.builder()
                .score(score)
                .status(status)
                .studentId(surveyResult.getStudentID())
                .build();
        }     

      public StatusStudent maptoResultStudent(SurveyQuestionOptions options, SurveyResult surveyResult){
        return StatusStudent.builder()
                .resultStd(options.getOptionText())
                .studentId(surveyResult.getStudentID())
                .valueOfQuestion(options.getScore())
                .build();
        }  

      public SurveyQuestionResultResponse mapToSurveyQuestionResponse(SurveyQuestions surveyQuestions,List<StatusStudent> answers) {
        return SurveyQuestionResultResponse.builder()
                .questionId(surveyQuestions.getQuestionID())
                .categoryName(String.valueOf(surveyQuestions.getCategory().getCategoryName()))
                .questionText(surveyQuestions.getQuestionText())
                .ansStudent(answers)
                .build();
        }


    public SurveyResultsResponse mapToSurveyResultsResponse1(Surveys survey, List<SurveyQuestionResultResponse> questions,List<StatusStudent> std) {
        return SurveyResultsResponse.builder()
                .surveyId(survey.getSurveyID())
                .surveyName(survey.getSurveyName())
                .description(survey.getDescription())
                .questions(questions)
                .std(std)
                .build();
        }

    public SurveyResultsResponse mapToSurveyUpdate(SurveyQuestions surveyQuestions, List<SurveyQuestionResultResponse> questions) {
        return SurveyResultsResponse.builder()
                .questions(questions)
                .surveyId(surveyQuestions.getSurveyID())
                .build();
    }

    public List<SurveyResultsResponse> getUserSurveyResults(List<SurveyQuestionOptionsChoices> surveyResults) {
        if (surveyResults == null) throw new ResourceNotFoundException("No survey results found");
        return buildSurveyResults(surveyResults);
    }

    public List<SurveyResultsResponse> buildSurveyResults(List<SurveyQuestionOptionsChoices> surveyResults) {

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


    public SurveyQuestionResultResponse mapToSurveyQuestionResultResponse(SurveyQuestionOptionsChoices result) {
        return SurveyQuestionResultResponse.builder()
                .questionId(result.getQuestionID())
                .categoryName(String.valueOf(result.getQuestion().getCategory().getCategoryName())) // No LazyInitializationException
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
                .createBy(survey.getCreator().getUsername())
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
                .createBy(survey.getCreator().getUsername())
                .build();
    }
}