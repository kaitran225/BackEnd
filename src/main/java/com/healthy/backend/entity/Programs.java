package com.healthy.backend.entity;

import com.healthy.backend.enums.ProgramStatus;
import com.healthy.backend.enums.ProgramType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

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
    private ProgramStatus status;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedByUser", referencedColumnName = "UserID", nullable = false)
    private Users createdByUser;

    @Column(name = "FacilitatorID", length = 36, nullable = false, insertable = false, updatable = false)
    private String facilitatorID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "FacilitatorID", referencedColumnName = "PsychologistID", nullable = false)
    private Psychologists psychologists;

    @Column(name = "StartDate")
    private LocalDate startDate;

    @Column(name = "EndDate")
    private LocalDate endDate;

    @Column(name = "MeetingLink", length = 255)
    private String meetingLink;

    @Enumerated(EnumType.STRING)
    @Column(name = "AppointmentType", length = 50, nullable = false)
    private ProgramType type;

    @Column(name = "DepartmentID", length = 36, nullable = false, insertable = false, updatable = false)
    private String departmentID;

    @Column(name = "Rating")
    private Integer rating;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DepartmentID", referencedColumnName = "DepartmentID", nullable = false)
    private Department department;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "ProgramTags",
            joinColumns = @JoinColumn(name = "ProgramId"),
            inverseJoinColumns = @JoinColumn(name = "TagId")
    )
    private Set<Tags> tags = new HashSet<>();

    @OneToMany(mappedBy = "programs", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Notifications> notifications = new HashSet<>();

    public Programs(
            String programID, String programName,
            String description, int maxParticipants, int duration,
            ProgramStatus status, Department department,
            Psychologists psychologists, HashSet<Tags> tags, LocalDate startDate,
            String meetingLink, ProgramType type, String userID) {
        this.programID = programID;
        this.programName = programName;
        this.description = description;
        this.numberParticipants = duration;
        this.duration = maxParticipants;
        this.status = status;
        this.department = department;
        this.psychologists = psychologists;
        this.tags = tags;
        this.startDate = startDate;
        this.type = type;
        this.endDate = startDate.plusDays(duration);
        this.meetingLink = meetingLink;
        this.facilitatorID = psychologists.getPsychologistID();
        this.departmentID = department.getDepartmentID();
    }

    public Programs(
            String programID, String programName,
            String description, int maxParticipants, int duration,
            ProgramStatus status, Department department,
            Psychologists psychologists, HashSet<Tags> tags, LocalDate startDate,
            String meetingLink, ProgramType type, Users createdByUser) {
        this.programID = programID;
        this.programName = programName;
        this.description = description;
        this.numberParticipants = maxParticipants;
        this.duration = duration;
        this.status = status;
        this.department = department;
        this.psychologists = psychologists;
        this.tags = tags;
        this.startDate = startDate;
        this.type = type;
        this.endDate = startDate.plusWeeks(duration);
        this.meetingLink = meetingLink;
        this.facilitatorID = psychologists.getPsychologistID();
        this.departmentID = department.getDepartmentID();
        this.createdByUser = createdByUser;
    }


    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }
} 