package com.healthy.backend.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VerificationResponse {

    @Schema(example = "Success")
    private String message;

    @Schema(example = "eyJhbG.eyJzdW.TmCs6rN")
    private String token;

    @Schema(example = "true")
    private boolean verified;
}
