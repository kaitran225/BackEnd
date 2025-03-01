package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "SurveyResult")
public class SurveyResult {

    @Id
    @Column(name = "ResultID", length = 36, nullable = false)
    private String resultID;

    @Column(name = "SurveyID", length = 36, nullable = false)
    private String surveyID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SurveyID", referencedColumnName = "SurveyID", insertable = false, updatable = false)
    private Surveys survey;

    @Column(name = "StudentID", length = 36, nullable = false)
    private String studentID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudentID", referencedColumnName = "StudentID", insertable = false, updatable = false)
    private Students student;

    @OneToMany(mappedBy = "surveyResult", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SurveyQuestionOptionsChoices> choices;

    public SurveyResult(String resultID, String surveyID, String studentID) {
        this.resultID = resultID;
        this.surveyID = surveyID;
        this.studentID = studentID;
    }
}
