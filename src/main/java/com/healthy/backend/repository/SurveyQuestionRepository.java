package com.healthy.backend.repository;

import com.healthy.backend.entity.SurveyQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestions, String> {
} 