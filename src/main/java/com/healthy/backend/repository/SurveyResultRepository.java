package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.SurveyResults;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SurveyResultRepository extends JpaRepository<SurveyResults, String> {
    List<SurveyResults> findByStudentID(String userId);
} 