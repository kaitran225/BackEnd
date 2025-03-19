package com.healthy.backend.entity;

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
@NoArgsConstructor
@Table(name = "Periodic")
public class Periodic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long periodicID;

    @ManyToOne
    @JoinColumn(name = "surveyID", referencedColumnName = "SurveyID", nullable = false)
    private Surveys survey;

    @Column(name = "startDate", nullable = false)
    private LocalDateTime startDate;

    @Column(name = "endDate", nullable = false)
    private LocalDateTime endDate;

    @Column(name = "duration", nullable = false)
    private Integer duration;

    public Periodic(Surveys surveyID, LocalDateTime startDate, LocalDateTime endDate) {
        this.survey = surveyID;
        this.startDate = startDate;
        this.duration = (int) startDate.until(endDate, java.time.temporal.ChronoUnit.DAYS);
        this.endDate = endDate;
    }
}
