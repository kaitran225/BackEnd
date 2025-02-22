package com.healthy.backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ParentRegisterRequest {
    @Schema(example = "username")
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    private String username;

    @Schema(example = "@User_password123")
    @NotBlank(message = "Password is required")
    @Size(min = 8, message = "Password must be at least 8 characters")
    @Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=]).*$",
            message = "Password must contain at least one digit, one lowercase, one uppercase, and one special character")
    private String password;

    @Schema(example = "John Doe")
    @NotBlank(message = "Full name is required")
    private String fullName;

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    @Schema(example = "user@example.com")
    private String email;

    @NotBlank(message = "Phone number is required")
    @Schema(example = "1234567890")
    private String phoneNumber;

    @Schema(example = "Example Address")
    private String address;

    @Schema(example = "Male")
    @NotBlank(message = "Gender is required")
    private String gender;

    @Schema(
            description = "A list of student IDs associated with the parent.",
            example = "[\"STU001\", \"STU002\", \"STU003\"]"
    )
    @NotBlank(message = "Student IDs are required")
    private List<String> StudentIds;
}