package com.healthy.BackEnd.DTO.Auth;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(example = "admin")
    @NotBlank(message = "Username or Email is required") // Changed message to be more generic
    private String loginIdentifier; // Combined email and username into a single field

    @Schema(example = "adminpass")
    @NotBlank(message = "Password is required")
    private String password;
}
