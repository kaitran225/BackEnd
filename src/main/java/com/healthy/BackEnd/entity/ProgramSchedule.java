package com.healthy.BackEnd.entity;

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
    @Column(name = "ScheduleID", length = 36, nullable = false)
    private String scheduleID;

    @Column(name = "ProgramID", length = 36, nullable = false)
    private String programID;

    @Enumerated(EnumType.STRING)
    @Column(name = "DayOfWeek", columnDefinition = "ENUM('Monday', 'Tuesday', 'Wednesday', 'Thursday', 'Friday', 'Saturday', 'Sunday')", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "StartTime")
    private LocalTime startTime;

    @Column(name = "EndTime")
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProgramID", referencedColumnName = "ProgramID", insertable = false, updatable = false)
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