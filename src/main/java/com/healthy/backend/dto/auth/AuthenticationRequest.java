package com.healthy.backend.dto.auth;

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
    @Schema(example = "admin@example.com")
    @NotBlank(message = "Username or Email is required")
    private String loginIdentifier;

    @Schema(example = "adminpass")
    @NotBlank(message = "Password is required")
    private String password;
}
