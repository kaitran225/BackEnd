package com.healthy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthy.backend.entity.SurveyResult;

public interface SurveyResultRepository extends JpaRepository<SurveyResult, String> {

    List<SurveyResult> findByStudentID(String id);
    List<SurveyResult> findBySurveyID(String surveyId);
    SurveyResult findByResultID(String resultId);
}