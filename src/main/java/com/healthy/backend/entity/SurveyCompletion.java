package com.healthy.backend.entity;

import java.time.LocalDateTime;
import java.util.UUID;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

@Table(name = "SurveyCompletion")
public class SurveyCompletion {

    @Id
    @Column(name = "id", length = 36)
    private String id = UUID.randomUUID().toString(); 

    @ManyToOne
    @JoinColumn(name = "StudentID", nullable = false)
    private Students student;

    @ManyToOne
    @JoinColumn(name = "SurveyID", nullable = false)  
    private Surveys survey;

    @Column(name = "CompleteDate", nullable = false)
    private LocalDateTime completionDate;
    
}    