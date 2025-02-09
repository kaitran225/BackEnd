package com.healthy.backend.repository;

import com.healthy.backend.entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointments, String> {
    List<Appointments> findByStudentID(String userId);
} 