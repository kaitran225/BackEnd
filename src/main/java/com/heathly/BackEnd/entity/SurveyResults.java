package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "SurveyResults")
public class SurveyResults {
    
    @Id
    @Column(name = "ResultID", length = 36, nullable = false)
    private String resultID;

    @Column(name = "StudentID", length = 36, nullable = false)
    private String studentID;

    @Column(name = "QuestionID", length = 36, nullable = false)
    private String questionID;

    @Column(name = "AnswerID", length = 36, nullable = false)
    private String answerID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "StudentID", referencedColumnName = "StudentID", insertable = false, updatable = false)
    private Students student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QuestionID", referencedColumnName = "QuestionID", insertable = false, updatable = false)
    private SurveyQuestions question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "AnswerID", referencedColumnName = "AnswerID", insertable = false, updatable = false)
    private Answers answer;
} 