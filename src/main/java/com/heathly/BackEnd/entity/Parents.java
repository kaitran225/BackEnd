package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Parents")
public class Parents {
    
    @Id
    @Column(name = "ParentID", length = 36, nullable = false)
    private String parentID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String userID;

    @Column(name = "ChildID", length = 36)
    private String childID;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ChildID", referencedColumnName = "StudentID", insertable = false, updatable = false)
    private Students child;
} 