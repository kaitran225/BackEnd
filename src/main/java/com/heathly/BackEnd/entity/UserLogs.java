package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "UserLogs")
public class UserLogs {
    
    @Id
    @Column(name = "LogID", length = 36)
    private String LogID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String UserID;

    @Column(name = "LoginTime", updatable = false)
    private LocalDateTime LoginTime;

    @Column(name = "IPAddress", length = 50)
    private String IPAddress;

    @ManyToOne
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private Users user;

    @PrePersist
    protected void onCreate() {
        LoginTime = LocalDateTime.now();
    }
} 