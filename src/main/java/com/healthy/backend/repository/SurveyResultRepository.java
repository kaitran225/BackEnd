package com.healthy.backend.repository;

import com.healthy.backend.entity.SurveyResults;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyResultRepository extends JpaRepository<SurveyResults, String> {
    List<SurveyResults> findByStudentID(String userId);
} 