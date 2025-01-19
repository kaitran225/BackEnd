package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.AppointmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentHistoryRepository extends JpaRepository<AppointmentHistory, String> {
} 