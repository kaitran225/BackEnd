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
@Table(name = "SurveyQuestionOptions")
public class SurveyQuestionOptions {

    @Id
    @Column(name = "OptionID", length = 36, nullable = false)
    private String optionID;

    @Column(name = "QuestionID", length = 36, nullable = false)
    private String questionID;

    @Column(name = "OptionText", columnDefinition = "TEXT", nullable = false)
    private String optionText;

    @Column(name = "Score")
    private Integer score;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "QuestionID", referencedColumnName = "QuestionID", insertable = false, updatable = false)
    private SurveyQuestions question;

    public SurveyQuestionOptions(String answerID, String questionID, String optionText, Integer score) {
        this.optionID = answerID;
        this.questionID = questionID;
        this.optionText = optionText;
        this.score = score;
    }
} 