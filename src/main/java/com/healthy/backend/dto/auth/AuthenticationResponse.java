package com.healthy.backend.dto.auth;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthenticationResponse {
    @Schema(example = "eyJhbG.eyJzdW.TmCs6rN")
    private String accessToken;

    @Schema(example = "eyJhbG.eyJzdW.TmCs6rN")
    private String refreshToken;

    @Schema(example = "12")
    private Integer expiresIn;

    @Schema(example = "2023-01-01")
    private String verificationExpiresAt;

    @Schema(example = "eyJhbG.eyJzdW.TmCs6rN")
    private String verificationToken;

    @Schema(example = "eyJhbG.eyJzdW.TmCs6rN")
    private String resetPasswordToken;

    @Schema(example = "2023-01-01")
    private String resetPasswordExpiresAt;

    @Schema(example = "true")
    private boolean verified;

    @Schema(example = "US001")
    private String userId;

    @Schema(example = "S001")
    private String studentId;

    @Schema(example = "PSY001")
    private String psychologistId;

    @Schema(example = "Name")
    private String fullName;

    @Schema(example = "ROlE")
    private String role;
} 