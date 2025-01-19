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
@Table(name = "AppointmentHistory")
public class AppointmentHistory {
    
    @Id
    @Column(name = "HistoryID", length = 36, nullable = false)
    private String historyID;

    @Column(name = "AppointmentID", length = 36, nullable = false)
    private String appointmentID;

    @Enumerated(EnumType.STRING)
    @Column(name = "Action", nullable = false)
    private Action action;

    @Enumerated(EnumType.STRING)
    @Column(name = "Status", nullable = false)
    private Status status;

    @Column(name = "ChangedBy", length = 36, nullable = false)
    private String changedBy;

    @Column(name = "ChangeDate", updatable = false)
    private LocalDateTime changeDate;

    @Column(name = "Notes", columnDefinition = "TEXT")
    private String notes;

    @ManyToOne
    @JoinColumn(name = "AppointmentID", insertable = false, updatable = false)
    private Appointments appointment;

    @ManyToOne
    @JoinColumn(name = "ChangedBy", insertable = false, updatable = false)
    private Users changedByUser;

    public AppointmentHistory(String h001, String appointmentID, Action action, Status status, String userId) {
    }

    @PrePersist
    protected void onCreate() {
        changeDate = LocalDateTime.now();
    }

    public enum Action {
        Created,
        Updated,
        Cancelled
    }

    public enum Status {
        Scheduled,
        Completed,
        Cancelled
    }
} 