package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@Table(name = "ProgramSchedule")
public class ProgramSchedule {
    
    @Id
    @Column(name = "ScheduleID", length = 36)
    private String ScheduleID;

    @Column(name = "ProgramID", length = 36, nullable = false)
    private String ProgramID;

    @Enumerated(EnumType.STRING)
    @Column(name = "DayOfWeek", columnDefinition = "ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')")
    private DayOfWeek DayOfWeek;

    @Column(name = "StartTime")
    private LocalTime StartTime;

    @Column(name = "EndTime")
    private LocalTime EndTime;

    @ManyToOne
    @JoinColumn(name = "ProgramID", insertable = false, updatable = false)
    private Programs program;

    public enum DayOfWeek {
        Monday,
        Tuesday,
        Wednesday,
        Thursday,
        Friday,
        Saturday,
        Sunday
    }
} 