package com.healthy.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ProgramSchedule")
public class ProgramSchedule {
    
    @Id
    @Column(name = "ScheduleID", length = 36, nullable = false)
    private String scheduleID;

    @Column(name = "ProgramID", length = 36, nullable = false)
    private String programID;

    @Column(name = "DayOfWeek", length = 10, nullable = false)
    private String dayOfWeek;

    @Column(name = "StartTime", nullable = false)
    private LocalTime startTime;

    @Column(name = "EndTime", nullable = false)
    private LocalTime endTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProgramID", referencedColumnName = "ProgramID", insertable = false, updatable = false)
    private Programs program;

    public ProgramSchedule(String sch001, String programID, String monday, LocalTime parse, LocalTime parse1) {
    }
} 