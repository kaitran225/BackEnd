package com.healthy.backend.repository;

import com.healthy.backend.entity.OnLeaveRequest;
import com.healthy.backend.entity.Psychologists;
import com.healthy.backend.enums.OnLeaveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface LeaveRequestRepository extends JpaRepository<OnLeaveRequest, String> {
    List<OnLeaveRequest> findByStatus(OnLeaveStatus status);
    List<OnLeaveRequest> findByPsychologistPsychologistID(String psychologistId);

    @Query("SELECT lr FROM OnLeaveRequest lr " +
            "WHERE lr.psychologist.psychologistID = :psychologistId " +
            "AND lr.status = :status " +
            "AND lr.startDate <= :date " +
            "AND lr.endDate >= :date")
    List<OnLeaveRequest> findByPsychologistPsychologistIDAndStatusAndDateRange(
            @Param("psychologistId") String psychologistId,
            @Param("status") OnLeaveStatus status,
            @Param("date") LocalDate date
    );

    @Query("SELECT l FROM OnLeaveRequest l WHERE " +
            "l.psychologist = :psychologist AND " +
            "l.status IN ('APPROVED', 'PENDING') AND " +
            "(l.startDate <= :endDate AND l.endDate >= :startDate)")
    List<OnLeaveRequest> findByPsychologistAndStatusAndDateRange(
            @Param("psychologist") Psychologists psychologist,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    List<OnLeaveRequest> findByPsychologistPsychologistIDAndStatus(
            @Param("psychologistId") String psychologistId,
            @Param("status") OnLeaveStatus status
    );

    @Query("SELECT l.leaveRequestID FROM OnLeaveRequest l ORDER BY l.leaveRequestID DESC LIMIT 1")
    String findLastLeaveRequestId();

}