package com.healthy.BackEnd.dto.auth;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @NotBlank(message = "Username or Email is required") // Changed message to be more generic
    private String loginIdentifier; // Combined email and username into a single field

    @NotBlank(message = "Password is required")
    private String password;
}
