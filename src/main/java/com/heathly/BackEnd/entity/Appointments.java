package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Appointments")
public class Appointments {
    
    @Id
    @Column(name = "AppointmentID", length = 36)
    private String AppointmentID;

    @Column(name = "TimeSlotsID", length = 36)
    private String TimeSlotsID;

    @Column(name = "StudentID", length = 36, nullable = false)
    private String StudentID;

    @Column(name = "PsychologistID", length = 36, nullable = false)
    private String PsychologistID;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", columnDefinition = "ENUM('Scheduled', 'Completed', 'Cancelled')")
    private Status Status;

    @Column(name = "Notes", columnDefinition = "TEXT")
    private String Notes;

    @Column(name = "MeetingLink", length = 255)
    private String MeetingLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "AppointmentType", columnDefinition = "ENUM('Online', 'Offline')")
    private AppointmentType AppointmentType;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime CreatedAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime UpdatedAt;

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
        Status = Status.Scheduled;
        AppointmentType = AppointmentType.Offline;
    }

    @PrePersist
    protected void onCreate() {
        CreatedAt = LocalDateTime.now();
        UpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = LocalDateTime.now();
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