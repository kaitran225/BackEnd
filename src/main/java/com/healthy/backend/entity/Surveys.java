package com.healthy.backend.entity;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import com.healthy.backend.enums.SurveyStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "Details", columnDefinition = "TEXT")
    private String details;

    @Column(name = "Duration", length = 100)
    private String duration;

    @Column(name = "CategoryID", length = 36, nullable = false)
    private String categoryID;

    @Column(name = "CreatedBy", length = 36, nullable = false)
    private String createdBy;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "StartDate", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "EndDate", nullable = false)
    private LocalDateTime endDate;

    @Transient
    private boolean isOpen;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", length = 50, nullable = false)
    private SurveyStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryID", referencedColumnName = "CategoryID", insertable = false, updatable = false)
    private Categories category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedBy", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users creator;

    @OneToMany(mappedBy = "surveys", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Notifications> notifications;

    public Surveys(String surveyID, String surveyName, String description, String categoryID, String createdBy, SurveyStatus status) {
        this.surveyID = surveyID;
        this.surveyName = surveyName;
        this.description = description;
        this.categoryID = categoryID;
        this.createdBy = createdBy;
        this.status = status;
    }
    
    public Surveys(String surveyID, String surveyName, String description, String categoryID, String createdBy, SurveyStatus status, LocalDateTime startDate, LocalDateTime endDate) {
        this.surveyID = surveyID;
        this.surveyName = surveyName;
        this.description = description;
        this.categoryID = categoryID;
        this.createdBy = createdBy;
        this.status = status;
        this.isOpen = false; 
        this.startDate = startDate;
        this.endDate = endDate;
    }


    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void updateSurvey() {
        this.isOpen = isSurveyOpen();
    }
    
    public boolean isSurveyOpen() {
        LocalDateTime now = LocalDateTime.now();
        return now.isAfter(startDate) && now.isBefore(endDate);
    }

    public void setSurveyForMonth(int year, int month) {
        LocalDate firstDayOfMonth = LocalDate.of(year, month, 1);
        LocalDate firstMonDay = firstDayOfMonth.with(TemporalAdjusters.firstInMonth(DayOfWeek.MONDAY));
        LocalDate endOfFirstWeek = firstMonDay.plusDays(6);

        this.startDate = firstMonDay.atStartOfDay();
        this.endDate = endOfFirstWeek.atTime(LocalTime.MAX);
    }
} 