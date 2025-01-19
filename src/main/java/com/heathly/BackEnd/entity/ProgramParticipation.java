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
    @Column(name = "ParticipationID", length = 36)
    private String ParticipationID;

    @Column(name = "StudentID", length = 36, nullable = false)
    private String StudentID;

    @Column(name = "ProgramID", length = 36, nullable = false)
    private String ProgramID;

    @Column(name = "StartDate")
    private LocalDate StartDate;

    @Column(name = "EndDate")
    private LocalDate EndDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", columnDefinition = "ENUM('In Progress', 'Completed', 'Cancelled')")
    private Status Status = Status.InProgress;

    @ManyToOne
    @JoinColumn(name = "StudentID", insertable = false, updatable = false)
    private Students student;

    @ManyToOne
    @JoinColumn(name = "ProgramID", insertable = false, updatable = false)
    private Programs program;

    public enum Status {
        InProgress,
        Completed,
        Cancelled
    }
} 