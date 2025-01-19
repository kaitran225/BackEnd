package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Surveys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Surveys, String> {
} 