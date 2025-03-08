package com.healthy.backend.entity;

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

    @Column(name = "DayOfWeek", length = 10, nullable = false)
    private String dayOfWeek;

    @Column(name = "StartTime", nullable = false)
    private LocalTime startTime;

    @Column(name = "EndTime", nullable = false)
    private LocalTime endTime;

    @Column(name = "ProgramID", length = 36, nullable = false)
    private String programID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Program", referencedColumnName = "ProgramID", nullable = false)
    private Programs program;

    public ProgramSchedule(String scheduleID, Programs program, String dayOfWeek, LocalTime startTime, LocalTime endTime) {
        this.scheduleID = scheduleID;
        this.program = program;
        this.dayOfWeek = dayOfWeek;
        this.startTime = startTime;
        this.endTime = endTime;
        this.programID = program.getProgramID();
    }
} 