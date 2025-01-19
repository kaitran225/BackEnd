package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "TimeSlots")
public class TimeSlots {
    
    @Id
    @Column(name = "TimeSlotsID", length = 36)
    private String TimeSlotsID;

    @Column(name = "PsychologistID", length = 36)
    private String PsychologistID;

    @Column(name = "SlotDate", nullable = false)
    private LocalDate SlotDate;

    @Column(name = "StartTime", nullable = false)
    private LocalTime StartTime;

    @Column(name = "EndTime", nullable = false)
    private LocalTime EndTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", columnDefinition = "ENUM('Available', 'Booked')")
    private Status Status = Status.Available;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime CreatedAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime UpdatedAt;

    @ManyToOne
    @JoinColumn(name = "PsychologistID", insertable = false, updatable = false)
    private Psychologists psychologist;

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
        Available,
        Booked
    }
} 