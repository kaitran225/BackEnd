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
@Table(name = "Notifications")
public class Notifications {
    
    @Id
    @Column(name = "NotificationID", length = 36)
    private String notificationID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String userID;

    @Column(name = "Title", length = 255, nullable = false)
    private String title;

    @Column(name = "Message", columnDefinition = "TEXT", nullable = false)
    private String message;

    @Enumerated(EnumType.STRING)
    @Column(name = "Type", nullable = false, columnDefinition = "ENUM('Appointment', 'Survey', 'Program', 'Done')")
    private Type type;

    @Column(name = "IsRead")
    private Boolean isRead;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @ManyToOne
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private Users user;

    public Notifications() {
        isRead = false;
    }

    public Notifications(String not001, String userId, String appointmentScheduled, String s, Type type) {
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }

    public enum Type {
        Appointment,
        Survey,
        Program,
        Done
    }
} 