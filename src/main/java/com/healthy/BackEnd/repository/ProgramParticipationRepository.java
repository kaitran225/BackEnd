package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.ProgramParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramParticipationRepository extends JpaRepository<ProgramParticipation, String> {
    List<ProgramParticipation> findByStudentID(String studentID);

    boolean existsByStudentID(String studentID);
} 