package com.healthy.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "Programs")
public class Programs {

    @Id
    @Column(name = "ProgramID", length = 36, nullable = false)
    private String programID;

    @Column(name = "ProgramName", length = 100, nullable = false)
    private String programName;

    @Enumerated(EnumType.STRING)
    @Column(name = "Category", length = 50, nullable = false, columnDefinition = "ENUM('Cognitive', 'Social', 'Emotional', 'Physical', 'Self Help', 'Wellness', 'Assessment', 'Support Group', 'Life Skills', 'Prevention', 'Counseling')")
    private Category category;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "NumberParticipants")
    private Integer numberParticipants;

    @Column(name = "Duration")
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 50, columnDefinition = "ENUM('Activate', 'Inactive')", nullable = false)
    private Status status;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "ManagedByStaffID", length = 36)
    private String managedByStaffID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ManagedByStaffID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users managedByStaff;

    public Programs(String programID, String programName, Category category, String description, Integer numberParticipants, Integer duration, Status status, String managedByStaffID) {
        this.programID = programID;
        this.programName = programName;
        this.category = category;
        this.description = description;
        this.numberParticipants = numberParticipants;
        this.duration = duration;
        this.managedByStaffID = managedByStaffID;
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public enum Category {
        Cognitive,
        Social,
        Emotional,
        Physical,
        SelfHelp,
        Wellness,
        Assessment,
        SupportGroup,
        LifeSkills,
        Prevention,
        Counseling
    }

    public enum Status {
        Activate,
        Inactive
    }
} 