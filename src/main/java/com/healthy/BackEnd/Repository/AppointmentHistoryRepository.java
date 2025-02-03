package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.AppointmentHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AppointmentHistoryRepository extends JpaRepository<AppointmentHistory, String> {
} 