package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "Users")
public class Users {
    
    @Id
    @Column(name = "UserID", length = 36)
    private String UserID;

    @Column(name = "Username", length = 50, nullable = false, unique = true) // Chỉ định rõ tên cột
    private String username;

    @Column(name = "PasswordHash", length = 255, nullable = false, columnDefinition = "VARCHAR(255)")
    private String PasswordHash;

    @Column(name = "FullName", length = 100, nullable = false)
    private String FullName;

    @Column(name = "Email", length = 100, unique = true)
    private String Email;

    @Column(name = "PhoneNumber", length = 15)
    private String PhoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", nullable = false, columnDefinition = "ENUM('Student', 'Parent', 'Psychologist', 'Manager', 'Staff')")
    private UserRole Role;

    @Column(name = "CreatedAt", updatable = false)
    private LocalDateTime CreatedAt;

    @Column(name = "UpdatedAt")
    private LocalDateTime UpdatedAt;

    @PrePersist
    protected void onCreate() {
        CreatedAt = LocalDateTime.now();
        UpdatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        UpdatedAt = LocalDateTime.now();
    }


    // Enum for Role
    public enum UserRole {
        Student,
        Parent,
        Psychologist,
        Manager,
        Staff
    }
}