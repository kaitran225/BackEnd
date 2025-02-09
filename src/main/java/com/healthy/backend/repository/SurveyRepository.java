package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.Surveys;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SurveyRepository extends JpaRepository<Surveys, String> {
} 