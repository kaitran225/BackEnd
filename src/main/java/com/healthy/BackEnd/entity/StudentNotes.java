package com.healthy.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "StudentNotes")
public class StudentNotes {
    
    @Id
    @Column(name = "NoteID", length = 36)
    private String noteID;

    @Column(name = "StudentID", length = 36, nullable = false)
    private String studentID;

    @Column(name = "PsychologistID", length = 36, nullable = false)
    private String psychologistID;

    @Column(name = "NoteText", columnDefinition = "TEXT", nullable = false)
    private String noteText;

    @Enumerated(EnumType.STRING)
    @Column(name = "NoteType", nullable = false, columnDefinition = "ENUM('General', 'Behavior', 'Academic', 'Emotional')")
    private NoteType noteType;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "StudentID", insertable = false, updatable = false)
    private Students student;

    @ManyToOne
    @JoinColumn(name = "PsychologistID", insertable = false, updatable = false)
    private Psychologists psychologist;

    public StudentNotes(String noteID, String studentID, String psychologistID, String noteText, NoteType noteType) {
        this.noteID = noteID;
        this.studentID = studentID;
        this.psychologistID = psychologistID;
        this.noteText = noteText;
        this.noteType = noteType;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum NoteType {
        General,
        Behavior,
        Academic,
        Emotional
    }
} 