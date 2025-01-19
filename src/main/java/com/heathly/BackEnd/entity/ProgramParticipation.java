package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Getter
@Setter
@Table(name = "ProgramParticipation")
public class ProgramParticipation {
    
    @Id
    @Column(name = "ParticipationID", length = 36, nullable = false)
    private String participationID;

    @Column(name = "StudentID", length = 36, nullable = false)
    private String studentID;

    @Column(name = "ProgramID", length = 36, nullable = false)
    private String programID;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", columnDefinition = "ENUM('In Progress', 'Completed', 'Cancelled')", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudentID", referencedColumnName = "StudentID", insertable = false, updatable = false)
    private Students student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ProgramID", referencedColumnName = "ProgramID", insertable = false, updatable = false)
    private Programs program;

    public ProgramParticipation() {status = Status.InProgress;
    }

    public enum Status {
        InProgress,
        Completed,
        Cancelled
    }
} 