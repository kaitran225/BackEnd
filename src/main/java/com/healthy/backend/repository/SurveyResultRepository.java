package com.healthy.backend.repository;

import com.healthy.backend.entity.SurveyResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

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

    @Query("SELECT sr FROM SurveyResult sr WHERE sr.surveyID = :surveyID AND sr.studentID = :studentID")
    List<SurveyResult> findBySurveyIDAndStudentID(@Param("surveyID") String surveyID, @Param("studentID") String studentID);

    @Query("SELECT COUNT(sr) FROM SurveyResult sr WHERE sr.surveyID = :surveyID AND sr.studentID =:studentID")
    int countResultStudent(@Param("surveyID") String surveyID, @Param("studentID") String studentID);


    @Query("SELECT COUNT(DISTINCT sr.studentID) FROM SurveyResult sr WHERE sr.surveyID = :surveyID")
    int countDistinctStudentsBySurveyID(@Param("surveyID") String surveyID);

    @Query("SELECT COUNT(sr) > 0 FROM SurveyResult sr " +
            "WHERE sr.surveyID = :surveyID " +
            "AND sr.studentID = :studentID " +
            "AND sr.completionDate BETWEEN :startDate AND :endDate")
    boolean existsBySurveyIDAndStudentIDAndCompletionDateBetween(
            @Param("surveyID") String surveyID,
            @Param("studentID") String studentID,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query("SELECT COUNT(DISTINCT sr.studentID) FROM SurveyResult sr " +
            "WHERE sr.surveyID = :surveyID " +
            "AND sr.completionDate BETWEEN :startDate AND :endDate")
    int countDistinctStudentsBySurveyIDAndCompletionDateBetween(
            @Param("surveyID") String surveyID,
            @Param("startDate") LocalDateTime startDate,
            @Param("endDate") LocalDateTime endDate
    );


    @Query("SELECT DISTINCT sr.studentID FROM SurveyResult sr WHERE sr.surveyID = :surveyID")
    List<String> findStudentsBySurveyID(@Param("surveyID") String surveyID);


    @Query("SELECT sr FROM SurveyResult sr JOIN FETCH sr.choices WHERE sr.resultID = :resultID")
    SurveyResult findByIdWithChoices(@Param("resultID") String resultID);

    SurveyResult findByStudentIDAndSurveyID(String studentID, String surveyID);
}