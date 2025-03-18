package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.*;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "SurveyID", referencedColumnName = "SurveyID", insertable = false, updatable = false)
    private Surveys survey;

    public SurveyQuestions(String questionID, String surveyID, String questionText) {
        this.questionID = questionID;
        this.surveyID = surveyID;
        this.questionText = questionText;
    }
} 