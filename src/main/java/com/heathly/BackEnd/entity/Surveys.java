package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Surveys")
public class Surveys {
    
    @Id
    @Column(name = "SurveyID", length = 36)
    private String SurveyID;

    @Column(name = "SurveyName", length = 100, nullable = false)
    private String SurveyName;

    @Column(name = "Description", columnDefinition = "TEXT")
    private String Description;

    @Column(name = "CategoryID", length = 36)
    private String CategoryID;

    @Column(name = "CreatedBy", length = 36, nullable = false)
    private String CreatedBy;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime CreatedAt;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", columnDefinition = "ENUM('Unfinished', 'Finished', 'Cancelled')")
    private Status Status = Status.Unfinished;

    @ManyToOne
    @JoinColumn(name = "CategoryID", insertable = false, updatable = false)
    private Categories category;

    @ManyToOne
    @JoinColumn(name = "CreatedBy", insertable = false, updatable = false)
    private Users createdByUser;

    @PrePersist
    protected void onCreate() {
        CreatedAt = LocalDateTime.now();
    }

    public enum Status {
        Unfinished,
        Finished,
        Cancelled
    }
} 