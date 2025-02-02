package com.healthy.BackEnd.Entity;

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
@Table(name = "Blog")
public class Blog {

    @Id
    @Column(name = "BlogID", length = 36)
    private String blogID;

    @Column(name = "Title", length = 100)
    private String title;

    @Column(name = "CreatedBy", length = 36)
    private String createdBy;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "Content", columnDefinition = "TEXT")
    private String content;

    @ManyToOne
    @JoinColumn(name = "CreatedBy", insertable = false, updatable = false)
    private Users createdByUser;

    public Blog(String blogID, String title, String createdBy, String content) {
        this.blogID = blogID;
        this.title = title;
        this.createdBy = createdBy;
        this.content = content;
    }

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
} 