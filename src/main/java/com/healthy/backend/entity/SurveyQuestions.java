package com.healthy.backend.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "SurveyQuestions")
public class SurveyQuestions {

    @Id
    @Column(name = "QuestionID", length = 36, nullable = false)
    private String questionID;

    @Column(name = "SurveyID", length = 36, nullable = false)
    private String surveyID;

    @Column(name = "QuestionText", columnDefinition = "TEXT", nullable = false)
    private String questionText;

    @Column(name = "CategoryID", length = 36)
    private String categoryID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SurveyID", referencedColumnName = "SurveyID", insertable = false, updatable = false)
    private Surveys survey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CategoryID", referencedColumnName = "CategoryID", insertable = false, updatable = false)
    private Categories category;

    public SurveyQuestions(String questionID, String surveyID, String questionText, String categoryID) {
        this.questionID = questionID;
        this.surveyID = surveyID;
        this.questionText = questionText;
        this.categoryID = categoryID;
    }
} 