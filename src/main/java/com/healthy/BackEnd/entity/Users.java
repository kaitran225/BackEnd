package com.healthy.BackEnd.entity;

import com.healthy.BackEnd.dto.UserDTO;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.List;

@Builder
@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "Users")
public class Users implements UserDetails {

    @Id
    @Column(name = "UserID", length = 36, nullable = false)
    private String userId;

    @Column(name = "Username", length = 50, nullable = false, unique = true)
    private String username;

    @Column(name = "PasswordHash", nullable = false)
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

    @Enumerated(EnumType.STRING)
    @Column(name = "Gender", columnDefinition = "ENUM('Male', 'Female', 'Other')")
    private Gender gender;

    @Column(name = "CreatedAt", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "UpdatedAt", nullable = false)
    private LocalDateTime updatedAt;

    @Column(name = "ResetToken")
    private String resetToken;

    public Users(String userId, String username, String passwordHash, String fullName, String email, String phoneNumber, UserRole userRole, Gender gender) {
        this.userId = userId;
        this.username = username;
        this.passwordHash = passwordHash;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.role = userRole;
        this.gender = gender;
    }

    public static Users fromDTO(UserDTO dto) {
        return Users.builder()
                .userId(dto.getUserId())
                .username(dto.getUsername())
                .passwordHash(dto.getPasswordHash())
                .fullName(dto.getFullName())
                .email(dto.getEmail())
                .phoneNumber(dto.getPhone())
                .role(UserRole.valueOf(dto.getRole()))
                .gender(Gender.valueOf(dto.getGender()))
                .build();
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

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority("ROLE_" + role.name()));
    }

    @Override
    public String getPassword() {
        return passwordHash;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public boolean isPresent() {
        return userId != null;
    }

    public enum UserRole {
        STUDENT,
        PARENT,
        PSYCHOLOGIST,
        MANAGER
    }

    public enum Gender {
        Male,
        Female,
        Other
    }
}