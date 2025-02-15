package com.healthy.backend.repository;

import com.healthy.backend.entity.Appointments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AppointmentRepository extends JpaRepository<Appointments, String> {

    List<Appointments> findByStudentID(String studentID);
    List<Appointments> findByPsychologistID(String psychologistID);
    // Đếm tổng số appointment hiện có
    @Query("SELECT COUNT(a) FROM Appointments a")
    long countTotalAppointments();
} 