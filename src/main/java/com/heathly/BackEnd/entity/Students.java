package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "Students")
public class Students {
    
    @Id
    @Column(name = "StudentID", length = 36)
    private String StudentID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String UserID;

    @Column(name = "Grade")
    private Integer Grade;

    @Column(name = "Class", length = 20)
    private String Class;

    @Column(name = "SchoolName", length = 100)
    private String SchoolName;

    @Enumerated(EnumType.STRING)
    @Column(name = "Gender", columnDefinition = "ENUM('Male', 'Female', 'Other')")
    private Gender Gender;

    @ManyToOne
    @JoinColumn(name = "UserID", insertable = false, updatable = false)
    private Users user;

    public enum Gender {
        Male,
        Female,
        Other
    }
} 