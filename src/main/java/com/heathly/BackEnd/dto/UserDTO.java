package com.heathly.BackEnd.dto;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private String userId;

    @NotBlank(message = "Username cannot be blank")
    @Size(min = 2, max = 50, message = "Username must be between 2 and 50 characters")
    private String username;

    @NotBlank(message = "Password cannot be blank")
    private String password;

    @NotBlank(message = "Full name cannot be blank")
    private String fullName;

    @Email(message = "Email should be valid")
    private String email;

    @Pattern(regexp = "/(84[3|5|7|8|9])+([0-9]{8})\\b/g;", message = "Phone number cannot be blank")
    private String phoneNumber;

    private String role;
    @Enumerated(EnumType.STRING)
    private String status;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
