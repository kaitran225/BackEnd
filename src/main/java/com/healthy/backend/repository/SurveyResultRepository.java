package com.healthy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthy.backend.entity.SurveyResult;

public interface SurveyResultRepository extends JpaRepository<SurveyResult, String> {

    List<SurveyResult> findByStudentID(String id);
    List<SurveyResult> findBySurveyID(String surveyId);
    SurveyResult findByResultID(String resultId);

    @Query("SELECT sr.studentID FROM SurveyResult sr WHERE sr.surveyID = :surveyID")
    List<String> findAllStudentIdsBySurveyID(@Param("surveyID") String surveyID);

    @Query("SELECT sr.resultID FROM SurveyResult sr ORDER BY sr.resultID DESC LIMIT 1")
    String findLastResultId();
}