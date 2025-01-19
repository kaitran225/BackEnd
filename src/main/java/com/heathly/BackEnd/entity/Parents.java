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
    @Column(name = "ParentID", length = 36)
    private String ParentID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String UserID;

    @Column(name = "ChildID", length = 36)
    private String ChildID;

    @ManyToOne
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private Users user;

    @ManyToOne
    @JoinColumn(name = "ChildID", insertable = false, updatable = false)
    private Students child;
} 