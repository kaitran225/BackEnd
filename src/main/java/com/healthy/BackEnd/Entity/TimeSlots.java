package com.healthy.BackEnd.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@Table(name = "TimeSlots")
public class TimeSlots {

    @Id
    @Column(name = "TimeSlotsID", length = 36, nullable = false)
    private String timeSlotsID;

    @Column(name = "PsychologistID", length = 36, nullable = false)
    private String psychologistID;

    @Column(name = "SlotDate", nullable = false)
    private LocalDate slotDate;

    @Column(name = "StartTime", nullable = false)
    private LocalTime startTime;

    @Column(name = "EndTime", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", columnDefinition = "ENUM('Available', 'Booked')", nullable = false)
    private Status status;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PsychologistID", referencedColumnName = "PsychologistID", insertable = false, updatable = false)
    private Psychologists psychologist;

    public TimeSlots() {
        status = Status.Available;
    }

    public TimeSlots(String timeSlotsID, String psychologistID, LocalDate slotDate, LocalTime startTime, LocalTime endTime, Status status) {
        this.timeSlotsID = timeSlotsID;
        this.psychologistID = psychologistID;
        this.slotDate = slotDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum Status {
        Available,
        Booked
    }
} 