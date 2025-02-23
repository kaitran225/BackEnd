package com.healthy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthy.backend.entity.SurveyQuestionOptions;
import com.healthy.backend.entity.SurveyQuestionOptionsChoices;
import org.springframework.stereotype.Repository;

@Repository
public interface SurveyQuestionOptionsChoicesRepository extends JpaRepository<SurveyQuestionOptionsChoices, String> {
    SurveyQuestionOptionsChoices findByQuestionIDAndOptionID(String questionId, String optionId);
    SurveyQuestionOptionsChoices findByOptionID(String optionId);
    List<SurveyQuestionOptionsChoices> findByQuestionID(String questionId);

//    @Query("SELECT s FROM SurveyQuestionOptionsChoices s " +
//            "JOIN FETCH s.question q " +
//            "JOIN FETCH q.category c " +
//            "JOIN FETCH s.option a " +
//            "WHERE s.studentID = :studentId")
//    List<SurveyQuestionOptionsChoices> findByStudentIDWithDetails(@Param("studentId") String studentId);

}