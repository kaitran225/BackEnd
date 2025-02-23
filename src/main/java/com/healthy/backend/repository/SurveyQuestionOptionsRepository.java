package com.healthy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.healthy.backend.entity.SurveyQuestionOptions;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface SurveyQuestionOptionsRepository extends JpaRepository<SurveyQuestionOptions, String> {
    SurveyQuestionOptions findByOptionID(String answerId);

    // @Query("SELECT an FROM Answers an ORDER BY an.answerID DESC")
    SurveyQuestionOptions findFirstByOrderByOptionIDDesc();

    List<SurveyQuestionOptions> findByQuestionID(String questionId);
}