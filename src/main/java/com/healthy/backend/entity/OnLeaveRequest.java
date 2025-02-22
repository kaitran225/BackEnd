package com.healthy.backend.entity;

import com.healthy.backend.enums.OnLeaveStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.util.Date;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "leave_requests")
public class OnLeaveRequest {
    @Id
    @Column(name = "LeaveRequestID", length = 36)
    private String leaveRequestID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "PsychologistID", nullable = false)
    private Psychologists psychologist;

    @Column(nullable = false)
    private LocalDate startDate;

    @Column(nullable = false)
    private LocalDate endDate;

    @Column(nullable = false)
    private String reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OnLeaveStatus status = OnLeaveStatus.PENDING;

    @CreationTimestamp
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdAt;

}