package com.healthy.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Notifications")
public class Notifications {
    
    @Id
    @Column(name = "NotificationID", length = 36)
    private String NotificationID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String UserID;

    @Column(name = "Title", length = 255, nullable = false)
    private String Title;

    @Column(name = "Message", columnDefinition = "TEXT", nullable = false)
    private String Message;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false, columnDefinition = "ENUM('Appointment', 'Survey', 'Program', 'Done')")
    private Type Type;

    @Column(name = "IsRead")
    private Boolean IsRead;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime CreatedAt;

    @ManyToOne
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private Users user;

    public Notifications() {
        IsRead = false;
    }

    @PrePersist
    protected void onCreate() {
        CreatedAt = LocalDateTime.now();
    }

    public enum Type {
        Appointment,
        Survey,
        Program,
        Done
    }
} 