package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.SurveyQuestions;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyQuestionRepository extends JpaRepository<SurveyQuestions, String> {
} 