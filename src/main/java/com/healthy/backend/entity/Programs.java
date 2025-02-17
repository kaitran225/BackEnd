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

    @Column(name = "FacilitatorID", length = 36, nullable = false,insertable=false, updatable=false)
    private String facilitatorID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FacilitatorID", referencedColumnName = "PsychologistID", nullable = false)
    private Psychologists psychologists;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "MeetingLink", length = 255)
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "AppointmentType", length =  50, nullable = false)
    private Type type;

    @Column(name = "DepartmentID", length = 36, nullable = false,insertable = false, updatable = false)
    private String departmentID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DepartmentID", referencedColumnName = "DepartmentID", nullable = false)
    private Department department;

    @ManyToMany
    @JoinTable(
            name = "ProgramTags",
            joinColumns = @JoinColumn(name = "ProgramId"),
            inverseJoinColumns = @JoinColumn(name = "TagId")
    )
    private Set<Tags> tags = new HashSet<>();

    public Programs(
            String programID,
            String programName,
            String description,
            int i, int i1, Status status,
            Department department,
            Psychologists psychologists,
            HashSet<Tags> tags,
            LocalDate startDate,
            String meetingLink,
            Type type) {
        this.programID = programID;
        this.programName = programName;
        this.description = description;
        this.numberParticipants = i;
        this.duration = i1;
        this.status = status;
        this.department = department;
        this.psychologists = psychologists;
        this.tags = tags;
        this.startDate = startDate;
        this.type = type;
        this.meetingLink = meetingLink;
        this.facilitatorID = psychologists.getPsychologistID();
        this.departmentID = department.getDepartmentID();
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

//    public enum Category {
//        Cognitive,
//        Social,
//        Emotional,
//        Physical,
//        SelfHelp,
//        Wellness,
//        Assessment,
//        SupportGroup,
//        LifeSkills,
//        Prevention,
//        Counseling
//    }

    public enum Type {
        Online,
        Offline
    }

    public enum Status {
        Active,
        Inactive
    }
} 