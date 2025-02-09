package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.ProgramParticipation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramParticipationRepository extends JpaRepository<ProgramParticipation, String> {
    List<ProgramParticipation> findByStudentID(String studentID);

    boolean existsByStudentID(String studentID);
} 