package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Psychologists")
public class Psychologists {
    
    @Id
    @Column(name = "PsychologistID", length = 36)
    private String PsychologistID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String UserID;

    @Column(name = "Specialization", length = 100)
    private String Specialization;

    @Column(name = "YearsOfExperience")
    private Integer YearsOfExperience;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", columnDefinition = "ENUM('Active', 'On Leave', 'Inactive')")
    private Status Status = Status.Active;

    @ManyToOne
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private Users user;

    public enum Status {
        Active,
        OnLeave,
        Inactive
    }
} 