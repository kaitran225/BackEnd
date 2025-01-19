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

    public Students(String studentID, String userID, Integer grade, String className, String schoolName, Gender gender) {
        this.studentID = studentID;
        this.userID = userID;
        this.grade = grade;
        this.className = className;
        this.schoolName = schoolName;
        this.gender = gender;
    }

    public enum Gender {
        Male,
        Female,
        Other
    }
} 