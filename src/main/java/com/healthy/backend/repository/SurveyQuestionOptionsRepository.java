package com.healthy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthy.backend.entity.SurveyQuestionOptions;

@Repository
public interface SurveyQuestionOptionsRepository extends JpaRepository<SurveyQuestionOptions, String> {
    SurveyQuestionOptions findByOptionID(String answerId);

    // @Query("SELECT an FROM Answers an ORDER BY an.answerID DESC")
    SurveyQuestionOptions findFirstByOrderByOptionIDDesc();

    List<SurveyQuestionOptions> findByQuestionID(String questionId);
    
}