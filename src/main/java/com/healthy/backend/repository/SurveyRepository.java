package com.healthy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthy.backend.entity.Surveys;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Surveys, String> {

    @Query("SELECT s.surveyID FROM Surveys s ORDER BY s.surveyID DESC LIMIT 1")
    String findLastSurveyId();

}