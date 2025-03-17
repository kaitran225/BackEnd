package com.healthy.backend.repository;

import com.healthy.backend.entity.ProgramParticipation;
import com.healthy.backend.enums.ParticipationStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ProgramParticipationRepository extends JpaRepository<ProgramParticipation, String> {
    @Query("SELECT COUNT(pp) FROM ProgramParticipation pp " +
            "WHERE pp.programID = :programId " +
            "AND pp.status = :status " +
            "AND (:startDate IS NULL OR pp.startDate >= :startDate) " +
            "AND (:endDate IS NULL OR pp.startDate <= :endDate)")
    Long countByProgramAndStatusAndDateRange(@Param("programId") String programId,
                                             @Param("status") ParticipationStatus status,
                                             @Param("startDate") LocalDate startDate,
                                             @Param("endDate") LocalDate endDate);

    List<ProgramParticipation> findByStudentID(String studentID);

    List<ProgramParticipation> findByProgramID(String programID);

    @Query("SELECT p.programID FROM ProgramParticipation p WHERE p.studentID = :studentID")
    List<String> findProgramIDsByStudentID(@Param("studentID") String studentID);

    @Query("SELECT p.participationID FROM ProgramParticipation p ORDER BY p.participationID DESC LIMIT 1")
    String findLastParticipationCode();

    boolean existsByStudentID(String studentID);

    boolean existsByProgramIDAndStudentID(String programID, String studentID);

    List<ProgramParticipation> findByProgramIDAndStudentID(String programID, String studentId);

    @Query("SELECT p.studentID FROM ProgramParticipation p WHERE p.programID = :programID")
    List<String> findStudentIDsByProgramID(@Param("programID") String programID);

    @Modifying
    @Transactional
    @Query("DELETE FROM ProgramParticipation p WHERE p.programID = :programId")
    void deleteByProgramId(@Param("programId") String programId);

    @Query("SELECT p.studentID FROM ProgramParticipation p WHERE p.programID = :programId AND p.status <> :excludedStatus")
    List<String> findActiveStudentIDsByProgramID(@Param("programId") String programId, @Param("excludedStatus") ParticipationStatus excludedStatus);

    @Query("SELECT p FROM ProgramParticipation p WHERE p.studentID = :studentId AND p.status <> :excludedStatus")
    List<ProgramParticipation> findActiveByStudentID(@Param("studentId") String studentId, @Param("excludedStatus") ParticipationStatus excludedStatus);

    List<ProgramParticipation> findByProgramIDAndStudentIDIn(String programId, List<String> studentIDs);

    @Query("SELECT p FROM ProgramParticipation p WHERE p.programID = :programId AND p.studentID IN :studentIDs")
    List<ProgramParticipation> findProgramParticipationsByProgramIdAndStudentIds(
            @Param("programId") String programId,
            @Param("studentIDs") List<String> studentIDs);

    @Query("SELECT COUNT(p) FROM ProgramParticipation p WHERE p.programID = :programID AND p.status = 'JOINED'")
    Long countActiveStudentsByProgramID(@Param("programID") String programID);


    ProgramParticipation findFirstByProgramIDAndStudentIDOrderByParticipationIDDesc(String programID, String studentID);
}