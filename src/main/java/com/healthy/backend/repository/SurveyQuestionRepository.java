package com.healthy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthy.backend.entity.SurveyQuestions;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestions, String> {
    List<SurveyQuestions> findByQuestionIDIn(List<String> QuestionId) ;
    SurveyQuestions findByQuestionIDAndSurveyID(String QuestionID, String surveyID);
    List<SurveyQuestions> findBySurveyID(String surveyID);

    // @Query("SELECT sq from SurveyQuestions sq ORDER BY sq.questionID DESC")
    SurveyQuestions findFirstByOrderByQuestionIDDesc();
    

} 