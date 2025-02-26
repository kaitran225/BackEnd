package com.healthy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.healthy.backend.entity.SurveyQuestionOptionsChoices;

@Repository
public interface SurveyQuestionOptionsChoicesRepository extends JpaRepository<SurveyQuestionOptionsChoices, String> {
    SurveyQuestionOptionsChoices findByQuestionIDAndOptionID(String questionId, String optionId);
    SurveyQuestionOptionsChoices findByOptionID(String optionId);
    List<SurveyQuestionOptionsChoices> findByQuestionID(String questionId);
    List<SurveyQuestionOptionsChoices> findByResultID(String resultId);

//    @Query("SELECT s FROM SurveyQuestionOptionsChoices s " +
//            "JOIN FETCH s.question q " +
//            "JOIN FETCH q.category c " +
//            "JOIN FETCH s.option a " +
//            "WHERE s.studentID = :studentId")
//    List<SurveyQuestionOptionsChoices> findByStudentIDWithDetails(@Param("studentId") String studentId);

}