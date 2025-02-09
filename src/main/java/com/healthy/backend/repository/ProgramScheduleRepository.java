package com.healthy.backend.repository;

import com.healthy.backend.entity.ProgramSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramScheduleRepository extends JpaRepository<ProgramSchedule, String> {
} 