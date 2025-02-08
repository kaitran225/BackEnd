package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.Programs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Programs, String> {
    List<Programs> findByManagedByStaffID(String userId);

    List<Programs> findByProgramID(String programID);
} 