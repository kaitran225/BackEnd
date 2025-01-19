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
@Table(name = "Students")
public class Students {
    
    @Id
    @Column(name = "StudentID", length = 36, nullable = false)
    private String studentID;

    @Column(name = "UserID", length = 36, nullable = false)
    private String userID;

    @Column(name = "Grade")
    private Integer grade;

    @Column(name = "ClassName", length = 20)
    private String className;

    @Column(name = "SchoolName", length = 100)
    private String schoolName;

    @Enumerated(EnumType.STRING)
    @Column(name = "Gender", columnDefinition = "ENUM('Male', 'Female', 'Other')")
    private Gender gender;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "UserID", referencedColumnName = "UserID", insertable = false, updatable = false)
    private Users user;

    public Students(String s001, String userId, int i, String a, String exampleHighSchool, Gender gender) {
    }

    public enum Gender {
        Male,
        Female,
        Other
    }
} 