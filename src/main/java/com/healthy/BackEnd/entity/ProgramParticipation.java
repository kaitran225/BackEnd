package com.healthy.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "ProgramParticipation")
public class ProgramParticipation {
    
    @Id
    @Column(name = "ParticipationID", length = 36, nullable = false)
    private String participationID;

    @Column(name = "StudentID", length = 36, nullable = false)
    private String studentID;

    @Column(name = "ProgramID", length = 36, nullable = false)
    private String programID;

    @Column(name = "StartDate", nullable = false)
    private LocalDate startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 50, columnDefinition = "ENUM('InProgress', 'Completed', 'Cancelled')", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudentID", referencedColumnName = "StudentID", insertable = false, updatable = false)
    private Students student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProgramID", referencedColumnName = "ProgramID", insertable = false, updatable = false)
    private Programs program;

    public ProgramParticipation(String participationID, String studentID, String programID, Status status, LocalDate startDate, LocalDate endDate) {
        this.participationID = participationID;
        this.studentID = studentID;
        this.programID = programID;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
    }

    public enum Status {
        InProgress,
        Completed,
        Cancelled
    }
} 