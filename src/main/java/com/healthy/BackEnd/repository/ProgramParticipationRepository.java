package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.ProgramParticipation;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramParticipationRepository extends JpaRepository<ProgramParticipation, String> {
    List<ProgramParticipation> findByStudentID(String studentID);
    boolean existsByStudentID(String studentID);
} 