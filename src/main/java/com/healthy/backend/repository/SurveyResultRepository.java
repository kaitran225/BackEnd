package com.healthy.backend.repository;

import com.healthy.backend.entity.SurveyResult;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface SurveyResultRepository extends JpaRepository<SurveyResult, String> {

    List<SurveyResult> findByStudentID(String id);
}