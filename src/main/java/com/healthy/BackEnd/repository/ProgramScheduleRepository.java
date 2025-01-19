package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.ProgramSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramScheduleRepository extends JpaRepository<ProgramSchedule, String> {
} 