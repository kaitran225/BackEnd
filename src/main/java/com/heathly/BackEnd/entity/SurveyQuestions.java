package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SurveyQuestions")
public class SurveyQuestions {
    
    @Id
    @Column(name = "QuestionID", length = 36)
    private String QuestionID;

    @Column(name = "SurveyID", length = 36, nullable = false)
    private String SurveyID;

    @Column(name = "QuestionText", columnDefinition = "TEXT", nullable = false)
    private String QuestionText;

    @Column(name = "CategoryID", length = 36)
    private String CategoryID;

    @ManyToOne
    @JoinColumn(name = "SurveyID", insertable = false, updatable = false)
    private Surveys survey;

    @ManyToOne
    @JoinColumn(name = "CategoryID", insertable = false, updatable = false)
    private Categories category;
} 