package com.healthy.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Blog")
public class Blog {
    
    @Id
    @Column(name = "BlogID", length = 36)
    private String BlogID;

    @Column(name = "Title", length = 100)
    private String Title;

    @Column(name = "CreatedBy", length = 36)
    private String CreatedBy;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime CreatedAt;

    @Column(name = "Content", columnDefinition = "TEXT")
    private String Content;

    @ManyToOne
    @JoinColumn(name = "CreatedBy", insertable = false, updatable = false)
    private Users createdByUser;

    @PrePersist
    protected void onCreate() {
        CreatedAt = LocalDateTime.now();
    }
} 