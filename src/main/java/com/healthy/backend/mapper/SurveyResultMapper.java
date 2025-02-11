package com.healthy.backend.mapper;

import com.healthy.backend.dto.survey.SurveyQuestionResultResponse;
import com.healthy.backend.dto.survey.SurveyResultsResponse;
import com.healthy.backend.entity.SurveyResults;
import com.healthy.backend.entity.Surveys;
import com.healthy.backend.entity.Users;
import com.healthy.backend.exception.ResourceNotFoundException;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class SurveyResultMapper {

    public List<SurveyResultsResponse> getUserSurveyResults(String userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + userId));
        return user.getRole() == Users.UserRole.STUDENT
                ? buildSurveyResults(studentRepository.findByUserID(userId).getStudentID())
                : null;
    }

    private List<SurveyResultsResponse> buildSurveyResults(String studentId) {
        List<SurveyResults> results = surveyResultRepository.findByStudentIDWithDetails(studentId);

        Map<String, List<SurveyQuestionResultResponse>> groupedResults =
                results
                        .stream()
                        .collect(Collectors.groupingBy(
                                result ->
                                        result.getQuestion().getSurveyID(),
                                Collectors.mapping(this::mapToSurveyQuestionResultResponse, Collectors.toList())
                        ));

        // Fetch all surveys in one query
        List<String> surveyIds = groupedResults.keySet().stream().toList();
        List<Surveys> surveys = surveyRepository.findAllById(surveyIds);
        Map<String, Surveys> surveyMap = surveys.stream()
                .collect(Collectors.toMap(Surveys::getSurveyID, s -> s));

        return groupedResults.entrySet().stream()
                .map(entry -> mapToSurveyResultsResponse(surveyMap.get(entry.getKey()), entry.getValue()))
                .toList();
    }


    private SurveyQuestionResultResponse mapToSurveyQuestionResultResponse(SurveyResults result) {
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

    private SurveyResultsResponse mapToSurveyResultsResponse(Surveys survey, List<SurveyQuestionResultResponse> questions) {
        return SurveyResultsResponse.builder()
                .surveyId(survey.getSurveyID())
                .surveyName(survey.getSurveyName())
                .description(survey.getDescription())
                .questions(questions)
                .build();
    }
}
