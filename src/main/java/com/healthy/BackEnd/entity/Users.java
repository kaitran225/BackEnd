package com.healthy.BackEnd.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class Users {

    @Id
    @Column(name = "UserID", length = 36, nullable = false)
    private String userId;

    @Column(name = "Username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "PasswordHash", length = 255, nullable = false)
    private String passwordHash;

    @Column(name = "FullName", length = 100, nullable = false)
    private String fullName;

    @Column(name = "Email", length = 100, unique = true)
    private String email;

    @Column(name = "PhoneNumber", length = 15)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "Role", nullable = false)
    private UserRole role;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;

    public Users(String u000, String admin, String adminpass, String adminUser, String mail, String number, UserRole userRole) {
    }

    @PrePersist
    protected void onCreate() {
        if (createdAt == null) {
            createdAt = LocalDateTime.now();
        }
        if (updatedAt == null) {
            updatedAt = LocalDateTime.now();
        }
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum UserRole {
        STUDENT,
        PARENT,
        PSYCHOLOGIST,
        MANAGER,
        STAFF
    }
}