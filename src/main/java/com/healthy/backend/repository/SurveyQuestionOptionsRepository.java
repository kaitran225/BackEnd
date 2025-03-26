package com.healthy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.healthy.backend.entity.SurveyQuestionOptions;

@Repository
public interface SurveyQuestionOptionsRepository extends JpaRepository<SurveyQuestionOptions, String> {
    SurveyQuestionOptions findByOptionID(String answerId);

    // @Query("SELECT an FROM Answers an ORDER BY an.answerID DESC")
    SurveyQuestionOptions findFirstByOrderByOptionIDDesc();

    List<SurveyQuestionOptions> findByQuestion_QuestionID(String questionId);

    @Query("SELECT sqc.optionID FROM SurveyQuestionOptions sqc ORDER BY sqc.optionID DESC LIMIT 1")
    String findLastQuestionOptionId();

    // List<SurveyQuestionOptions> deleteByQuestionID(String questionID);

    @Modifying
    @Transactional
    @Query("DELETE FROM SurveyQuestionOptions o WHERE o.question.questionID IN :questionIds")
    void deleteByQuestionIds(@Param("questionIds") List<String> questionIds);

    @Query("SELECT o FROM SurveyQuestionOptions o WHERE o.question.questionID IN :questionIds")
    List<SurveyQuestionOptions> findAllByQuestionIds(@Param("questionIds") List<String> questionIds);
}