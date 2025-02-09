package com.healthy.backend.repository;

import com.healthy.backend.entity.ProgramParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramParticipationRepository extends JpaRepository<ProgramParticipation, String> {
    List<ProgramParticipation> findByStudentID(String studentID);

    boolean existsByStudentID(String studentID);
} 