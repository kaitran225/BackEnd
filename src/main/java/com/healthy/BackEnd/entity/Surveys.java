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
@AllArgsConstructor
@Table(name = "Surveys")
public class Surveys {
    
    @Id
    @Column(name = "SurveyID", length = 36, nullable = false)
    private String surveyID;

    @Column(name = "SurveyName", length = 100, nullable = false)
    private String surveyName;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String description;

    @Column(name = "CategoryID", length = 36, nullable = false)
    private String categoryID;

    @Column(name = "CreatedBy", length = 36, nullable = false)
    private String createdBy;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", columnDefinition = "ENUM('Unfinished', 'Finished', 'Cancelled')", nullable = false)
    private Status status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryID", referencedColumnName = "CategoryID", insertable = false, updatable = false)
    private Categories category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CreatedBy", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users creator;

    public Surveys() {
        status = Status.Unfinished;
    }

    public Surveys(String sur001, String stressSurvey, String surveyToAssessStressLevels, String categoryID, String userId) {
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
    }

    public enum Status {
        Unfinished,
        Finished,
        Cancelled
    }
} 