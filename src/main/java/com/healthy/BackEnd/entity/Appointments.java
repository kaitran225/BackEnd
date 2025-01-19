package com.healthy.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

import static com.healthy.BackEnd.entity.Appointments.AppointmentType.Offline;
import static com.healthy.BackEnd.entity.Appointments.Status.Scheduled;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "Appointments")
public class Appointments {
    
    @Id
    @Column(name = "AppointmentID", length = 36)
    private String appointmentID;

    @Column(name = "TimeSlotsID", length = 36)
    private String timeSlotsID;

    @Column(name = "StudentID", length = 36, nullable = false)
    private String studentID;

    @Column(name = "PsychologistID", length = 36, nullable = false)
    private String psychologistID;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", columnDefinition = "ENUM('Scheduled', 'Completed', 'Cancelled')")
    private Status status;

    @Column(name = "Notes", columnDefinition = "TEXT")
    private String notes;

    @Column(name = "MeetingLink", length = 255)
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "AppointmentType", columnDefinition = "ENUM('Online', 'Offline')")
    private AppointmentType appointmentType;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime updatedAt;

    @ManyToOne
    @JoinColumn(name = "StudentID", insertable = false, updatable = false)
    private Students student;

    @ManyToOne
    @JoinColumn(name = "TimeSlotsID", insertable = false, updatable = false)
    private TimeSlots timeSlot;

    @ManyToOne
    @JoinColumn(name = "PsychologistID", insertable = false, updatable = false)
    private Psychologists psychologist;

    public Appointments() {
        status = Scheduled;
        appointmentType = Offline;
    }

    public Appointments(String app001, String timeSlotsID, String studentID, String psychologistID, String url) {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Status {
        Scheduled,
        Completed,
        Cancelled
    }

    public enum AppointmentType {
        Online,
        Offline
    }
} 