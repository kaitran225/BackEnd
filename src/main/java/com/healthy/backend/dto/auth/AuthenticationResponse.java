package com.healthy.backend.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    @Schema(example = "aaaa.bbbb.cccc")
    private String accessToken;
    @Schema(example = "aaaa.bbbb.cccc")
    private String refreshToken;
    @Schema(example = "U00X")
    private String userId;
    @Schema(example = "ROlE")
    private String role;
} 