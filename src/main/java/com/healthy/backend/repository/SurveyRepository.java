package com.healthy.backend.repository;

import com.healthy.backend.entity.Surveys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Surveys, String> {
} 