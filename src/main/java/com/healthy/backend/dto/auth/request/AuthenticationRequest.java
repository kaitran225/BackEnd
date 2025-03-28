package com.healthy.backend.dto.auth.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationRequest {

    @Schema(example = "admin@example.com")
    @NotBlank(message = "Username or Email is required")
    private String loginIdentifier;

    @Schema(example = "adminpass")
    @NotBlank(message = "Password is required")
    private String password;
}
