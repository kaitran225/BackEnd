package com.healthy.backend.repository;

import com.healthy.backend.entity.Appointments;
import com.healthy.backend.enums.AppointmentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface AppointmentRepository extends JpaRepository<Appointments, String> {

    List<Appointments> findByStudentID(String studentID);
    List<Appointments> findByPsychologistID(String psychologistID);
    @Query("SELECT a.appointmentID FROM Appointments a ORDER BY a.appointmentID DESC LIMIT 1")
    String findLastAppointmentId();

    Page<Appointments> findByPsychologistIDAndStatusAndFeedbackNotNull(String psychologistId, AppointmentStatus status, Pageable pageable);
    List<Appointments> findByPsychologistIDAndStatusAndFeedbackNotNull(String psychologistId, AppointmentStatus status);


} 