package com.healthy.backend.dto.user;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class UsersRequest {
    @Schema(example = "54833773-8bd3-4688-a423-e1cda6ccffa3")
    @NotBlank(message = "User ID is required")
    private String userId;

    private String username;

    private String email;
}