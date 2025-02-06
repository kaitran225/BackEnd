package com.healthy.BackEnd.Repository;

import com.healthy.BackEnd.Entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointments, String> {
    List<Appointments> findByStudentID(String studentID);

    List<Appointments> findByPsychologistID(String psychologistID);
} 