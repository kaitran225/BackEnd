package com.healthy.backend.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.healthy.backend.entity.SurveyResult;

public interface SurveyResultRepository extends JpaRepository<SurveyResult, String> {

    List<SurveyResult> findByStudentID(String id);
    List<SurveyResult> findBySurveyID(String surveyId);
    SurveyResult findByResultID(String resultId);

    @Query("SELECT sr.studentID FROM SurveyResult sr WHERE sr.surveyID = :surveyID")
    List<String> findAllStudentIdsBySurveyID(@Param("surveyID") String surveyID);

    @Query("SELECT sr.resultID FROM SurveyResult sr ORDER BY sr.resultID DESC LIMIT 1")
    String findLastResultId();

    @Query("SELECT sr.resultID FROM SurveyResult sr WHERE sr.studentID = :studentId AND sr.surveyID = :surveyId")
    List<String> findStudentIdANDSurveyId(@Param("surveyId") String surveyId, @Param("studentId") String studentId);

    @Query("SELECT COUNT(sr) > 0 FROM SurveyResult sr WHERE sr.surveyID = :surveyID AND sr.studentID =:studentID")
    boolean existsBySurveyIDAndStudentID(@Param("surveyID") String surveyID, @Param("studentID") String studentID);

    @Query("SELECT COUNT(sr) FROM SurveyResult sr WHERE sr.surveyID = :surveyId")
    int countResultInSuv(@Param("surveyId") String surveyId);

    @Query("SELECT sr FROM SurveyResult sr WHERE sr.surveyID = :surveyID AND sr.studentID =:studentID")
    List<SurveyResult> findSurveyIDAndStudentID1(@Param("surveyID") String surveyID, @Param("studentID") String studentID);

    // boolean existsBySurveyIDAndStudentID(String surveyID, String studentID);
}