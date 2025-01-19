package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "StudentNotes")
public class StudentNotes {
    
    @Id
    @Column(name = "NoteID", length = 36)
    private String NoteID;

    @Column(name = "StudentID", length = 36, nullable = false)
    private String StudentID;

    @Column(name = "PsychologistID", length = 36, nullable = false)
    private String PsychologistID;

    @Column(name = "NoteText", columnDefinition = "TEXT", nullable = false)
    private String NoteText;

    @Enumerated(EnumType.STRING)
    @Column(name = "NoteType", nullable = false, columnDefinition = "ENUM('General', 'Behavior', 'Academic', 'Emotional')")
    private NoteType NoteType;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime CreatedAt;

    @ManyToOne
    @JoinColumn(name = "StudentID", insertable = false, updatable = false)
    private Students student;

    @ManyToOne
    @JoinColumn(name = "PsychologistID", insertable = false, updatable = false)
    private Psychologists psychologist;

    @PrePersist
    protected void onCreate() {
        CreatedAt = LocalDateTime.now();
    }

    public enum NoteType {
        General,
        Behavior,
        Academic,
        Emotional
    }
} 