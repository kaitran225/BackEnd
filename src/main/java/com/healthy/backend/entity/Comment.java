package com.healthy.backend.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "Comments")
@Getter
@Setter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(name = "ProgramID", length = 36)
    private String programID;

    @ManyToOne
    @JoinColumn(name = "ProgramID", insertable = false, updatable = false)
    private Programs programs;

    @Column(name = "ArticleID", length = 36)
    private String articleID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ArticleId", referencedColumnName = "ArticleID")
    private Article article;

    @Column(name = "SurveyID", length = 36)
    private String surveyID;

    @ManyToOne
    @JoinColumn(name = "SurveyID", insertable = false, updatable = false)
    private Surveys surveys;

    @Column(name = "AppointmentID", length = 36)
    private String appointmentID;

    @ManyToOne
    @JoinColumn(name = "AppointmentID", insertable = false, updatable = false)
    private Appointments appointment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID")
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ParentCommentId")
    private Comment parentComment;

    @OneToMany(mappedBy = "parentComment", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Comment> replies = new HashSet<>();

    @Column(name = "Rating")
    private Integer rating;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
