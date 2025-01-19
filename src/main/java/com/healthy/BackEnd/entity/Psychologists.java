package com.healthy.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Psychologists")
public class Psychologists {
    
    @Id
    @Column(name = "PsychologistID", length = 36, nullable = false)
    private String psychologistID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String userID;

    @Column(name = "Specialization", length = 100)
    private String specialization;

    @Column(name = "YearsOfExperience")
    private Integer yearsOfExperience;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", columnDefinition = "ENUM('Active', 'On Leave', 'Inactive')", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users user;

    public Psychologists() {
        status = Status.Active;
    }

    public enum Status {
        Active,
        OnLeave,
        Inactive
    }
} 