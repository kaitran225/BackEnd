package com.healthy.backend.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.healthy.backend.entity.Surveys;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SurveyRepository extends JpaRepository<Surveys, String> {

}