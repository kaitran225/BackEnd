package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Answers")
public class Answers {
    
    @Id
    @Column(name = "AnswerID", length = 36)
    private String AnswerID;

    @Column(name = "QuestionID", length = 36, nullable = false)
    private String QuestionID;

    @Column(name = "Answer", columnDefinition = "TEXT", nullable = false)
    private String Answer;

    @Column(name = "Score")
    private Integer Score;

    @ManyToOne
    @JoinColumn(name = "QuestionID", insertable = false, updatable = false)
    private SurveyQuestions question;
} 