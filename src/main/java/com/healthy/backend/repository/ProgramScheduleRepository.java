package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.ProgramSchedule;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProgramScheduleRepository extends JpaRepository<ProgramSchedule, String> {
} 