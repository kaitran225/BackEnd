package com.healthy.BackEnd.repository;

import com.healthy.BackEnd.entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointments, String> {
    List<Appointments> findByStudentID(String userId);
    List<Appointments> findByPsychologistID(String psychologistID);
} 