package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "AppointmentHistory")
public class AppointmentHistory {
    
    @Id
    @Column(name = "HistoryID", length = 36)
    private String HistoryID;

    @Column(name = "AppointmentID", length = 36, nullable = false)
    private String AppointmentID;

    @Enumerated(EnumType.STRING)
    @Column(name = "Action", nullable = false, columnDefinition = "ENUM('Created', 'Updated', 'Cancelled', 'Completed')")
    private Action Action;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false, columnDefinition = "ENUM('Scheduled', 'Completed', 'Cancelled')")
    private Status Status;

    @Column(name = "ChangedBy", length = 36, nullable = false)
    private String ChangedBy;

    @Column(name = "ChangeDate", updatable = false)
    private LocalDateTime ChangeDate;

    @Column(name = "Notes", columnDefinition = "TEXT")
    private String Notes;

    @ManyToOne
    @JoinColumn(name = "AppointmentID", insertable = false, updatable = false)
    private Appointments appointment;

    @ManyToOne
    @JoinColumn(name = "ChangedBy", insertable = false, updatable = false)
    private Users changedByUser;

    @PrePersist
    protected void onCreate() {
        ChangeDate = LocalDateTime.now();
    }

    public enum Action {
        Created,
        Updated,
        Cancelled,
        Completed
    }

    public enum Status {
        Scheduled,
        Completed,
        Cancelled
    }
} 