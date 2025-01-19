package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Programs")
public class Programs {
    
    @Id
    @Column(name = "ProgramID", length = 36)
    private String ProgramID;

    @Column(name = "ProgramName", length = 100, nullable = false)
    private String ProgramName;

    @Enumerated(EnumType.STRING)
    @Column(name = "Category", nullable = false, columnDefinition = "ENUM('Cognitive', 'Social', 'Emotional', 'Physical', 'Self Help', 'Wellness', 'Assessment', 'Support Group', 'Life Skills', 'Prevention', 'Counseling')")
    private Category Category;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String Description;

    @Column(name = "NumberParticipants")
    private Integer NumberParticipants;

    @Column(name = "Duration")
    private Integer Duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", columnDefinition = "ENUM('Activate', 'Inactive')")
    private Status Status = Status.Activate;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime CreatedAt;

    @Column(name = "ManagedByStaffID", length = 36)
    private String ManagedByStaffID;

    @ManyToOne
    @JoinColumn(name = "ManagedByStaffID", insertable = false, updatable = false)
    private Users managedByStaff;

    @PrePersist
    protected void onCreate() {
        CreatedAt = LocalDateTime.now();
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