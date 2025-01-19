package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.SurveyResults;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyResultRepository extends JpaRepository<SurveyResults, String> {
    List<SurveyResults> findByStudentID(String userId);
} 