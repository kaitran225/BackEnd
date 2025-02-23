package com.healthy.backend.repository;

import com.healthy.backend.entity.SurveyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Collection;
import java.util.List;

public interface SurveyResultRepository extends JpaRepository<SurveyResults, String> {
    List<SurveyResults> findByStudentID(String userId);
    Answers findByQuestionIDAndAnswerID(String questionId, String answerId);
    List<SurveyResults> findByQuestionID(String questionId);
    SurveyResults findByAnswerID(String answerId);

    List<SurveyResult> findByStudentID(String id);

    @Query("SELECT sr FROM SurveyResults sr " +
            "JOIN FETCH sr.question q " +
            "JOIN FETCH q.category c " +
            "JOIN FETCH sr.answer a " +
            "WHERE sr.studentID = :studentId")
    List<SurveyResults> findByStudentIDWithDetails(@Param("studentId") String st
}