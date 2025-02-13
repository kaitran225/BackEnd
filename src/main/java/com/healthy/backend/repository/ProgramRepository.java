package com.healthy.backend.repository;

import com.healthy.backend.entity.Programs;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProgramRepository extends JpaRepository<Programs, String> {
    List<Programs> findByManagedByStaffID(String userId);

} 