package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.SurveyQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestions, String> {
} 