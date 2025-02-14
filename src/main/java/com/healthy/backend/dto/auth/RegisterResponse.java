package com.healthy.backend.dto.auth;

import io.swagger.v3.oas.annotations.media.Schema;

public class RegisterResponse {
    @Schema(example = "U00X")
    private String userId;
    @Schema(example = "ROlE")
    private String role;
}
