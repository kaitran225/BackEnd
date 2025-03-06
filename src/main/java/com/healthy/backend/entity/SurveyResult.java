package com.healthy.backend.entity;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

    @Column(name = "Result")
    private Integer result;

    @Column(name = "MaxScore")
    private Integer maxScore;

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

    public SurveyResult(String resultID, String surveyID, String studentID, Integer result, Integer maxScore) {
        this.resultID = resultID;
        this.surveyID = surveyID;
        this.studentID = studentID;
        this.result = result;
        this.maxScore = maxScore;
    }
}
