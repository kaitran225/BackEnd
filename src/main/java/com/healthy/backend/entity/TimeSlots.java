package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "TimeSlots")
public class TimeSlots {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID) // Tự động tạo UUID
    @Column(name = "TimeSlotsID", length = 36, nullable = false, unique = true)
    private String timeSlotsID;

    @Column(name = "SlotDate", nullable = false)
    private LocalDate slotDate;

    @Column(name = "StartTime", nullable = false)
    private LocalTime startTime;

    @Column(name = "EndTime", nullable = false)
    private LocalTime endTime;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PsychologistID", nullable = false)
    private Psychologists psychologist;

    public enum Status {
        Available,
        Booked
    }


    public TimeSlots(LocalDate date, LocalTime startTime, LocalTime endTime, Psychologists psychologist) {
        this.slotDate = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.status = Status.Available;
        this.psychologist = psychologist;
    }
}