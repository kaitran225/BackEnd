package com.heathly.BackEnd.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class Users {

    @Id
    @Column(length = 36)
    private String UserID;

    @Column(name = "username", length = 50, nullable = false, unique = true) // Chỉ định rõ tên cột
    private String username;

    @Column(length = 255, nullable = false)
    private String PasswordHash;

    @Column(length = 100, nullable = false)
    private String FullName;

    @Column(length = 100, unique = true)
    private String Email;

    @Column(length = 15)
    private String PhoneNumber;


    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('Student', 'Parent', 'Psychologist', 'Manager', 'Staff')")
    private UserRole Role;

    @Column(updatable = false)
    private LocalDateTime CreatedAt;

    @Column
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