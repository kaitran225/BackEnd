package com.healthy.backend.entity;

import com.healthy.backend.enums.SurveyCategory;
import com.healthy.backend.enums.SurveyStandardType;
import com.healthy.backend.enums.SurveyStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Surveys")
public class Surveys {

    @Id
    @Column(name = "SurveyID", length = 36, nullable = false)
    private String surveyID;

    @Column(name = "SurveyName", length = 100, nullable = false)
    private String surveyName;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "CreatedBy", length = 36, nullable = false)
    private String createdBy;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "StartDate", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "Duration")
    private Integer duration;

    @Enumerated(EnumType.STRING)
    @Column(name = "StandardType", length = 50, nullable = false)
    private SurveyStandardType standardType;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 50, nullable = false)
    private SurveyStatus status;

    @Enumerated(EnumType.STRING)
    @Column(name = "Category", length = 50, nullable = false)
    private SurveyCategory category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedBy", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users creator;

    @OneToMany(mappedBy = "survey", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Periodic> periodic;

    @OneToMany(mappedBy = "surveys", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notifications> notifications;

    public Surveys(String surveyID, String surveyName, String description, String createdBy, LocalDate startDate, Integer duration, SurveyStandardType standardType) {
        this.surveyID = surveyID;
        this.surveyName = surveyName;
        this.description = description;
        this.duration = duration;
        this.createdBy = createdBy;
        this.startDate = startDate.atStartOfDay();
        this.endDate = startDate.plusWeeks(duration).atStartOfDay();
        this.standardType = standardType;
        this.category = cat(standardType);
    }

    public Surveys(String surveyID, String surveyName, String description, String createdBy, LocalDate startDate, Integer duration, SurveyStandardType standardType, SurveyStatus status) {
        this.surveyID = surveyID;
        this.surveyName = surveyName;
        this.description = description;
        this.duration = duration;
        this.createdBy = createdBy;
        this.startDate = startDate.atStartOfDay();
        this.endDate = startDate.plusWeeks(duration).atStartOfDay();
        this.standardType = standardType;
        this.category = cat(standardType);
        this.status = status;
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
            status = SurveyStatus.ACTIVE;
        }
    }

    public boolean isSurveyOpen() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }

    private SurveyCategory cat(SurveyStandardType standardType) {
        switch (standardType) {
            case SurveyStandardType.GAD_7 -> {
                return SurveyCategory.ANXIETY;
            }
            case SurveyStandardType.PSS_10 -> {
                return SurveyCategory.STRESS;
            }
            case SurveyStandardType.PHQ_9 -> {
                return SurveyCategory.DEPRESSION;
            }
            default -> {
                return null;
            }
        }
    }
} 