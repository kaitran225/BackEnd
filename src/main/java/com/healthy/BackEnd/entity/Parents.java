package com.healthy.BackEnd.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
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

    public Parents(String parentID, String userID, String childID) {
        this.parentID = parentID;
        this.userID = userID;
        this.childID = childID;
    }
} 