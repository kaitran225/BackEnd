package com.healthy.backend.repository;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.healthy.backend.entity.SurveyCompletion;




@Repository
public interface SurveyCompletionRepository extends JpaRepository<SurveyCompletion, String> {
    
    
    @Query("SELECT sc FROM SurveyCompletion sc WHERE sc.student.studentID = :studentId AND sc.survey.surveyID = :surveyId")
    List<SurveyCompletion> findCompleteStudentIdANDSurveyId(@Param("surveyId") String surveyId, @Param("studentId") String studentId);
}    