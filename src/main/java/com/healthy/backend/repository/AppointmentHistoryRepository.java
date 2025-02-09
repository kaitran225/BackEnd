package com.healthy.backend.repository;

import com.healthy.backend.entity.AppointmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentHistoryRepository extends JpaRepository<AppointmentHistory, String> {
} 