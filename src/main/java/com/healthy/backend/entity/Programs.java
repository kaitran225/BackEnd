package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
@Table(name = "Programs")
public class Programs {

    @Id
    @Column(name = "ProgramID", length = 36, nullable = false)
    private String programID;

    @Column(name = "ProgramName", length = 100, nullable = false)
    private String programName;

    @Enumerated(EnumType.STRING)
    @Column(name = "Category", length = 50, nullable = false)
    private Category category;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "NumberParticipants")
    private Integer numberParticipants;

    @Column(name = "Duration")
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 50, nullable = false)
    private Status status;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ManagedByStaffID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users managedByStaff;

    @Column(name = "ManagedByStaffID", length = 36, nullable = false)
    private String managedByStaffID;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "MeetingLink", length = 255)
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "AppointmentType", length =  50, nullable = false)
    private Type type;

    @ManyToMany
    @JoinTable(
            name = "ProgramTags",
            joinColumns = @JoinColumn(name = "ProgramId"),
            inverseJoinColumns = @JoinColumn(name = "TagId")
    )
    private Set<Tags> tags = new HashSet<>();

    public Programs(
            String prg001,
            String stressManagement,
            Category category,
            String programToHelpManageStress,
            int i, int i1, Status status,
            String staffUser, HashSet<Tags> tags,
            LocalDate startDate,
            String meetingLink,
            Type type) {
        this.programID = prg001;
        this.programName = stressManagement;
        this.category = category;
        this.description = programToHelpManageStress;
        this.numberParticipants = i;
        this.duration = i1;
        this.status = status;
        this.managedByStaffID = staffUser;
        this.tags = tags;
        this.startDate = startDate;
        this.type = type;
        this.meetingLink = meetingLink;
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

    public enum Type {
        Online,
        Offline
    }

    public enum Status {
        Activate,
        Inactive
    }
} 