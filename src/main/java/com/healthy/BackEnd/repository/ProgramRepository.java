package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Programs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Programs, String> {
    List<Programs> findByManagedByStaffID(String userId);
} 